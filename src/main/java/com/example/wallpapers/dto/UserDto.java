package com.example.wallpapers.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private String email;
    private Boolean showFavourites;
    private String registrationDate;
    private String userStatus;
}
