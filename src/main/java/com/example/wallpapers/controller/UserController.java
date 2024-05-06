package com.example.wallpapers.controller;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.dto.UserDto;
import com.example.wallpapers.enums.PostSort;
import com.example.wallpapers.jwt.JwtAuthentication;
import com.example.wallpapers.pojo.ChangePasswordRequest;
import com.example.wallpapers.pojo.StatusRequest;
import com.example.wallpapers.service.PostService;
import com.example.wallpapers.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
public class UserController {
    private final PostService postService;
    private final UserService userService;

    @Autowired
    public UserController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/id/{user_id}")
    @ResponseBody
    public UserDto getUser(@PathVariable("user_id") Long targetId, JwtAuthentication auth) {
        return userService.getUserInfo(auth.getId(), targetId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/id/{user_id}/uploads")
    @ResponseBody
    public Page<PostDto> getUserUploads(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "sort", defaultValue = "UPLOAD_DATE_DESC") PostSort sort,
            @PathVariable("user_id") Long targetId,
            JwtAuthentication auth
    ) {
        return postService.getUploadedByUser(page, size, sort, targetId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/id/{user_id}/favourites")
    @ResponseBody
    public Page<PostDto> getUserFavourites(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "sort", defaultValue = "ADDED_DATE_DESC") PostSort sort,
            @PathVariable("user_id") Long targetId,
            JwtAuthentication auth
    ) {
        return postService.getFavouritesByUser(page, size, sort, auth.getId(), targetId);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{user_id}/status")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void setUserStatus(
            @PathVariable("user_id") Long targetId,
            @RequestBody StatusRequest status
    ) {
        userService.setUserStatus(targetId, status.getStatus());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/id/{user_id}/password")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(
            @PathVariable("user_id") Long targetId,
            JwtAuthentication auth,
            @RequestBody ChangePasswordRequest request
    ) {
        userService.changeUserPassword(
                request.getOldPassword(), request.getNewPassword(),
                auth.getId(), targetId
        );
    }

}
