package com.example.wallpapers.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@Getter
@RequiredArgsConstructor
public enum PostSort {
    //ASC - по возрастанию
    //DESC - по убыванию
    ADDED_DATE_DESC(Sort.by(Sort.Direction.DESC, "addedDate")),
    ADDED_DATE_ASC(Sort.by(Sort.Direction.ASC, "addedDate")),
    UPLOAD_DATE_DESC(Sort.by(Sort.Direction.DESC, "uploadDate")),
    UPLOAD_DATE_ASC(Sort.by(Sort.Direction.ASC, "uploadDate")),
    // мб сделать поиск по нижним фильтрам платным
    VIEWS_DESC(Sort.by(Sort.Direction.DESC, "views")),
    VIEWS_ASC(Sort.by(Sort.Direction.ASC, "views")),
    DOWNLOADS_DESC(Sort.by(Sort.Direction.DESC, "downloads")),
    DOWNLOADS_ASC(Sort.by(Sort.Direction.ASC, "downloads")),
    RATING_DESC(Sort.by(Sort.Direction.DESC, "rating")),
    RATING_ASC(Sort.by(Sort.Direction.ASC, "rating"));
    private final Sort sortValue;
}
