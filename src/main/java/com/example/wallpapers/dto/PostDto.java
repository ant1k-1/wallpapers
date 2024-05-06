package com.example.wallpapers.dto;

import com.example.wallpapers.enums.PostStatus;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private Long postId;
    private Long userId;
    private String source;
    private String dimensions;
    private Long size;
    private PostStatus postStatus;
    private List<TagDto> postTags;
    private Long views;
    private Long downloads;
    private Long likes;
    private String image;
    private String preview;
    private String uploadDate;
}
