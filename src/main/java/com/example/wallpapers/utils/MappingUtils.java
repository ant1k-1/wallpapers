package com.example.wallpapers.utils;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.dto.TagDto;
import com.example.wallpapers.dto.UserDto;
import com.example.wallpapers.entity.Post;
import com.example.wallpapers.entity.Tag;
import com.example.wallpapers.entity.User;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Locale;

@Component
public class MappingUtils {
    private static final DateTimeFormatter postFormatter = DateTimeFormatter
            .ofPattern("d MMM yyyy hh:mm:ss", Locale.ENGLISH);
    private static final DateTimeFormatter userFormatter = DateTimeFormatter
            .ofPattern("d MMM yyyy", Locale.ENGLISH);

    public static PostDto mapToPostDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setPostId(post.getPostId());
        postDto.setUserId(post.getUser().getUserId());
        postDto.setSource(post.getSource());
        postDto.setDimensions(post.getDimensions());
        postDto.setSize(post.getSize());
        postDto.setPostStatus(post.getPostStatus());
        postDto.setPostTags(
                post.getPostTags()
                        .stream()
                        .map(MappingUtils::mapToTagDto)
                        .sorted(Comparator.comparing(TagDto::getTagName, String.CASE_INSENSITIVE_ORDER))
                        .toList());
        postDto.setViews(post.getViews());
        postDto.setDownloads(post.getDownloads());
        postDto.setLikes(post.getLikes());
        postDto.setImage(post.getImageUUID());
        postDto.setPreview(post.getPreviewUUID());
        postDto.setUploadDate(post.getUploadDate().format(postFormatter));
        return postDto;
    }

    public static TagDto mapToTagDto(Tag tag) {
        if (tag == null) return null;
        TagDto tagDto = new TagDto();
        tagDto.setTagId(tag.getTagId());
        tagDto.setTagType(tag.getTagType().name());
        tagDto.setTagName(tag.getTagName());
        tagDto.setUsageCount(tag.getUsageCount());
        return tagDto;
    }

    public static UserDto mapToUserDto(User user, boolean showEmail) {
        UserDto userDto = new UserDto();
        userDto.setUserId(user.getUserId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(showEmail ? user.getEmail() : "hidden");
        userDto.setRegistrationDate(user.getRegistrationDate().format(userFormatter));
        userDto.setShowFavourites(user.getShowFavourites());
        userDto.setUserStatus(user.getStatus().name());
        return userDto;
    }
}
