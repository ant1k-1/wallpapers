package com.example.wallpapers.utils;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.dto.TagDto;
import com.example.wallpapers.dto.UserDto;
import com.example.wallpapers.model.Post;
import com.example.wallpapers.model.Tag;
import com.example.wallpapers.model.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;
import java.util.stream.Collectors;

@Component
public class MappingUtils {
    private static final DateTimeFormatter formatDMMMYYYY = DateTimeFormatter
            .ofPattern("d MMM yyyy", Locale.ENGLISH);

    public static PostDto mapToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setPostStatus(post.getPostStatus());
        postDto.setPostTags(
                post.getPostTags()
                        .stream()
                        .map(MappingUtils::mapToTagDto)
                        .sorted(Comparator.comparing(TagDto::getTagName, String.CASE_INSENSITIVE_ORDER))
                        .toList());
        postDto.setViews(post.getViews());
        postDto.setDownloads(post.getDownloads());
        postDto.setRating(post.getRating() > 0 ? post.getRating() : 0);
        postDto.setImage(post.getImageUUID());
        postDto.setPreview(post.getPreviewUUID());
        postDto.setUploadDate(post.getUploadDate().format(formatDMMMYYYY));
        return postDto;
    }

    public static TagDto mapToTagDto(Tag tag) {
        TagDto tagDto = new TagDto();
        tagDto.setTagId(tag.getTagId());
        tagDto.setTagType(tag.getTagType().name());
        tagDto.setTagName(tag.getTagName());
        tagDto.setUsageCount(tag.getUsageCount());
        return tagDto;
    }

    public static UserDto mapToUserDto(User user, boolean showEmail, boolean showPosts) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(showEmail ? user.getEmail() : "hidden");
        userDto.setRegistrationDate(user.getRegistrationDate().format(formatDMMMYYYY));
        userDto.setPosts(user.getPosts()
                .stream()
                .sorted(Comparator.comparing(Post::getUploadDate))
                .map(MappingUtils::mapToPostDto)
                .toList());
        return userDto;
    }

}
