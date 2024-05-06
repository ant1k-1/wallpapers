package com.example.wallpapers.repository;

import com.example.wallpapers.entity.Tag;
import com.example.wallpapers.enums.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagTypeAndTagName(TagType tagType, String tagName);
    Optional<Tag> findByTagName(String tagName);
    List<Tag> findAllByTagNameStartingWith(String prefix);
    List<Tag> findAllByTagNameContains(String substring);
}
