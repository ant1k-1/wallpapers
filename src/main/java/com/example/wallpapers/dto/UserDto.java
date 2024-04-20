package com.example.wallpapers.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private String registrationDate;
    private List<PostDto> posts;
}
