package com.example.wallpapers.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagNamesRequest {
    private String[] tagNames;

    @Override
    public String toString() {
        return "TagNamesRequest{" +
                "tagNames=" + Arrays.toString(tagNames) +
                '}';
    }
}
