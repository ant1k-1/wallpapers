package com.example.wallpapers.service;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.entity.*;
import com.example.wallpapers.enums.PostSort;
import com.example.wallpapers.enums.PostStatus;
import com.example.wallpapers.enums.UserStatus;
import com.example.wallpapers.exception.BadArgumentException;
import com.example.wallpapers.exception.NotFoundException;
import com.example.wallpapers.pojo.ImageMetadata;
import com.example.wallpapers.pojo.TagNamesRequest;
import com.example.wallpapers.repository.FavouriteRepository;
import com.example.wallpapers.repository.PostRepository;
import com.example.wallpapers.repository.UserRepository;
import com.example.wallpapers.utils.MappingUtils;
import jakarta.transaction.Transactional;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagService tagService;
    private final FavouriteRepository favouriteRepository;
    private final HighResolutionStorage highResolutionStorage;
    private final LowResolutionStorage lowResolutionStorage;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository,
                       TagService tagService,
                       FavouriteRepository favouriteRepository,
                       HighResolutionStorage highResolutionStorage,
                       LowResolutionStorage lowResolutionStorage
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.tagService = tagService;
        this.favouriteRepository = favouriteRepository;
        this.highResolutionStorage = highResolutionStorage;
        this.lowResolutionStorage = lowResolutionStorage;
    }

    public Page<PostDto> getAllPosts(int page, int size, PostSort sort) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        return postRepository.findAll(pageable)
                .map(MappingUtils::mapToPostDto);
    }

    public Page<PostDto> getAllByPostStatus(int page, int size, PostStatus status, PostSort sort) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        return postRepository.findAllByPostStatus(status, pageable)
                .map(MappingUtils::mapToPostDto);
    }

    public Page<PostDto> getAllByTags(int page, int size, PostSort sort, TagNamesRequest tagNamesRequest) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        Set<Tag> tags = tagService.tagNamesToTags(tagNamesRequest.getTagNames());
        return postRepository.findAllByPostTagsIn(tags, tags.size(), pageable)
                .map(MappingUtils::mapToPostDto);
    }

    public Page<PostDto> getUploadedByUser(int page, int size, PostSort sort, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id='" + userId + "' not found"));
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        return postRepository.findAllByUser(user, pageable).map(MappingUtils::mapToPostDto);
    }

    public Page<PostDto> getFavouritesByUser(int page, int size, PostSort sort, Long userId, Long targetId) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundException("User id='" + targetId + "' not found"));
        boolean isOwner = userId.equals(targetId);
        return isOwner || user.getShowFavourites()
                ? postRepository.findFavouritesByUserId(targetId, pageable).map(MappingUtils::mapToPostDto)
                : Page.empty();
    }

    public PostDto getById(Long postId) {
        return MappingUtils.mapToPostDto(
                postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"))
        );
    }

    @Transactional
    public void create(Map<String, String> map, MultipartFile image, Long userId) {
        if (image.isEmpty()) throw new BadArgumentException("No image provided");
        Tika tika = new Tika();
        String detectedType;
        try {
            detectedType = tika.detect(image.getBytes());
        } catch (IOException e) {
            throw new BadArgumentException(e.getMessage());
        }

        if (!(
                detectedType.equals("image/jpeg")
                || detectedType.equals("image/png")
        )) {
            throw new BadArgumentException("Invalid file MIME type '" + detectedType + "'");
        }
        if (detectedType.equals("image/webp")) {
            throw new BadArgumentException("Requires high-resolution MIME type (png or jpeg) but not '" + detectedType + "'");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id='" + userId + "' not found"));
        if (!user.getStatus().equals(UserStatus.USER_ACTIVATED))
            throw new BadArgumentException("User with status='" + user.getStatus().name() + "' is not allowed to upload images");
        Post post = new Post();
        post.setUser(user);
        try {
            post.setSource(Objects.requireNonNull(map.get("source")));
            post.setPostStatus(PostStatus.POST_MODERATION);
            post.setPostTags(tagService.extractTags(map));
            post.setViews(0L);
            post.setDownloads(0L);
            post.setLikes(0L);
            String uuid = UUID.randomUUID().toString();
            String imageName = uuid + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());
            String previewName = uuid + "." + "webp";
            highResolutionStorage.save(image, imageName);
            ImageMetadata imageMetadata = lowResolutionStorage.save(image, previewName);
            post.setDimensions(imageMetadata.getDimensions());
            post.setSize(imageMetadata.getSize());
            post.setImageUUID(imageName);
            post.setPreviewUUID(previewName);
            post.setUploadDate(LocalDateTime.now());
            postRepository.save(post);

        } catch (NullPointerException ex) {
            throw new BadArgumentException(ex.getMessage());
        }
    }

    public void update(Long postId, Map<String, String> map, MultipartFile image) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
        try {
            if (!image.isEmpty()) {
                highResolutionStorage.update(image, post.getImageUUID());
                lowResolutionStorage.update(image, post.getPreviewUUID());
            }
            post.setSource(Objects.requireNonNull(map.get("source")));
            post.setPostTags(tagService.extractTags(map));
            postRepository.save(post);
        } catch (NullPointerException ex) {
            throw new BadArgumentException(ex.getMessage());
        }
    }

    public void remove(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
        for (var tag : post.getPostTags()) {
            tagService.decreaseTag(tag);
        }
        highResolutionStorage.delete(post.getImageUUID());
        lowResolutionStorage.delete(post.getPreviewUUID());
        postRepository.delete(post);
    }

    public void setPostStatus(Long postId, String status) {
        Post post  = postRepository.findById(postId).orElseThrow(
                () -> new NotFoundException("Post id='" + postId + "' not found"));
        try {
            post.setPostStatus(PostStatus.valueOf(status));
        } catch (IllegalArgumentException ex) {
            throw new BadArgumentException("Status '" + status + "' is invalid");
        }
    }

    public void addFavourites(Long postId, Long userId) {
        FavouriteId favouriteId = new FavouriteId(userId, postId);
        if (!favouriteRepository.existsById(favouriteId)) {
            Post post  = postRepository.findById(postId)
                    .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("User id='" + userId + "' not found"));
            post.setLikes(post.getLikes() + 1);
            Favourite fav = new Favourite(user, post);
            favouriteRepository.save(fav);
            postRepository.save(post);
        }

    }
    @Transactional
    public void removeFavourite(Long postId, Long userId){
        Favourite favourite = favouriteRepository.findById(new FavouriteId(userId, postId))
                .orElseThrow(() -> new NotFoundException(
                        "Favourite user_id='" + userId + "' & post_id='" + postId +"' not found"));
        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
        favouriteRepository.delete(favourite);
        post.setLikes(post.getLikes() - 1);
        postRepository.save(post);
    }

    public void setPreviewQuality(Integer percent) {
        float percentFloat = (float) Math.round(percent * 100.0f) / 10000.0f;
        lowResolutionStorage.setPreviewQuality(percentFloat);
    }

    public void addPostTag(Long postId, Tag tag) {
        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
        if (post.getPostTags().contains(tag)) {
            throw new BadArgumentException("Post already contains this tag");
        } else {
            post.getPostTags().add(tag);
            postRepository.save(post);
        }
    }

    public void removePostTag(Long postId, Tag tag) {
        Post post  = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException("Post id='" + postId + "' not found"));
        if (!post.getPostTags().contains(tag)) {
            throw new BadArgumentException("Post already does not contain this tag");
        } else {
            post.getPostTags().remove(tag);
            tagService.decreaseTag(tag);
            postRepository.save(post);
        }
    }
}
