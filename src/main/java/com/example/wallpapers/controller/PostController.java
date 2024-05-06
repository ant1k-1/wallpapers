package com.example.wallpapers.controller;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.dto.TagDto;
import com.example.wallpapers.enums.PostSort;
import com.example.wallpapers.enums.PostStatus;
import com.example.wallpapers.jwt.JwtAuthentication;
import com.example.wallpapers.pojo.StatusRequest;
import com.example.wallpapers.pojo.TagNamesRequest;
import com.example.wallpapers.service.PostService;
import com.example.wallpapers.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@RequestMapping("/api/posts")
@RestController
public class PostController {
    private final PostService postService;
    private final TagService tagService;

    @Autowired
    public PostController(PostService postService, TagService tagService) {
        this.postService = postService;
        this.tagService = tagService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void createPost(
            @RequestParam Map<String, String> metadata,
            @RequestParam("pic") MultipartFile image,
            JwtAuthentication auth
    ) {
        postService.create(metadata, image, auth.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/id/{post_id}")
    @ResponseBody
    public PostDto getPostById(@PathVariable("post_id") Long postId)
    {
        return postService.getById(postId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all")
    @ResponseBody
    public Page<PostDto> getAllPosts(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "sort", defaultValue = "UPLOAD_DATE_DESC") PostSort sort
    ) {
        return postService.getAllPosts(page, size, sort);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/all/tags")
    @ResponseBody
    public Page<PostDto> getAllPostsByTags(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "sort", defaultValue = "UPLOAD_DATE_DESC") PostSort sort,
            @RequestBody TagNamesRequest tagNamesRequest
            ) {
        return postService.getAllByTags(page, size, sort, tagNamesRequest);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @GetMapping("/all/status")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Page<PostDto> getAllByPostStatus(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "3") Integer size,
            @RequestParam(value = "status", defaultValue = "POST_MODERATION") PostStatus status,
            @RequestParam(value = "sort", defaultValue = "UPLOAD_DATE_DESC") PostSort sort
    ) {
        return postService.getAllByPostStatus(page, size, status, sort);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/id/{post_id}/like")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void addFavourite(
            @PathVariable("post_id") Long postId,
            JwtAuthentication auth
    ) {
        postService.addFavourites(postId, auth.getId());
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/id/{post_id}/unlike")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void removeFavourite(
            @PathVariable("post_id") Long postId,
            JwtAuthentication auth
    ) {
        postService.removeFavourite(postId, auth.getId());
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(
            @PathVariable("post_id") Long postId,
            @RequestParam Map<String, String> metadata,
            @RequestParam("pic") MultipartFile image
    ) {
        postService.update(postId, metadata, image);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/delete")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void removePost(
            @PathVariable("post_id") Long postId
    ) {
        postService.remove(postId);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/status")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void setPostStatus(
            @PathVariable("post_id") Long postId,
            @RequestBody StatusRequest status
    ) {
        postService.setPostStatus(postId, status.getStatus());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/set_preview_quality/{percent}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void setPreviewQuality(
            @PathVariable("percent") Integer percent
    ) {
        postService.setPreviewQuality(percent);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/tag/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updatePostTag(
            @RequestBody TagDto tagDto
    ) {
        tagService.update(tagDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/tag/remove")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void removePostTag(
            @PathVariable("post_id") Long postId,
            @RequestBody TagDto tagDto
    ) {
        postService.removePostTag(postId, tagService.getTagByDto(tagDto));
    }

    @PreAuthorize("hasAnyRole('ROLE_MODER', 'ROLE_ADMIN')")
    @PostMapping("/id/{post_id}/tag/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void createPostTag(
            @PathVariable("post_id") Long postId,
            @RequestBody TagDto tagDto
    ) {
        postService.addPostTag(postId, tagService.create(postId, tagDto));
    }


}
