package com.example.wallpapers.dto;

import com.example.wallpapers.enums.TagType;
import lombok.Data;

@Data
public class TagDto {
    private Long tagId;
    private String tagType;
    private String tagName;
    private Long usageCount;
}
