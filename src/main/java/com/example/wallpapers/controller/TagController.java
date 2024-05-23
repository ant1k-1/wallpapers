package com.example.wallpapers.controller;

import com.example.wallpapers.dto.PostDto;
import com.example.wallpapers.dto.TagDto;
import com.example.wallpapers.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/tags")
@RestController
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{startsWith}")
    @ResponseBody
    public List<TagDto> getTagsStartingWith(
            @PathVariable("startsWith") String startsWith
    )
    {
        return tagService.getTagsStartingWith(startsWith);
    }
}
