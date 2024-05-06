package com.example.wallpapers.pojo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class ImageMetadata {
    private final String dimensions;
    private final Long size;
}
