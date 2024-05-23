package com.example.wallpapers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum UserSort {
    USER_ID_DESC(Sort.by(Sort.Direction.DESC, "userId")),
    USER_ID_ASC(Sort.by(Sort.Direction.ASC, "userId"));
    private final Sort sortValue;
}
