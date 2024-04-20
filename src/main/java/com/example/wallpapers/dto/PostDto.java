package com.example.wallpapers.dto;

import com.example.wallpapers.enums.Status;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private Long postId;
    private Status postStatus;
    private List<TagDto> postTags;
    private Long views;
    private Long downloads;
    private Long rating; //must be > 0
    private String image; //ready link
    private String preview; //ready link
    private String uploadDate;
}
