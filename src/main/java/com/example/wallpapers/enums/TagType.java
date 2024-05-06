package com.example.wallpapers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TagType {
    TAG("tag"),
    AUTHOR("author"),
    TITLE("title");

    private final String value;
}
