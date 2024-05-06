package com.example.wallpapers.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PostStatus {
    POST_PUBLISHED("POST_PUBLISHED"),
    POST_MODERATION("POST_MODERATION"),
    POST_HIDDEN("POST_HIDDEN"),
    POST_DELETED("POST_DELETED");
    private final String value;
}
