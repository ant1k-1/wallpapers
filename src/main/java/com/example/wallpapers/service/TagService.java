package com.example.wallpapers.service;

import com.example.wallpapers.dto.TagDto;
import com.example.wallpapers.entity.Tag;
import com.example.wallpapers.enums.TagType;
import com.example.wallpapers.exception.BadArgumentException;
import com.example.wallpapers.exception.NotFoundException;
import com.example.wallpapers.repository.TagRepository;
import com.example.wallpapers.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TagService {
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag create(Long postId, TagDto tagDto) {
        Tag tag = tagRepository.findByTagTypeAndTagName(
                TagType.valueOf(tagDto.getTagType()), tagDto.getTagName()
        ).orElse(null);
        if (tag != null) {
            return tag;
        } else {
            Tag newTag = new Tag();
            newTag.setTagName(tagDto.getTagName());
            try {
                newTag.setTagType(TagType.valueOf(tagDto.getTagType()));
            } catch (IllegalArgumentException e) {
                throw new BadArgumentException("Bad tag type '" + tagDto.getTagType() + "'");
            }
            if (postId != -1) {
                newTag.setUsageCount(1L);
                return tagRepository.save(newTag);
            }
        }
        return null;
    }

    public TagDto getTagByName(String name) {
        return MappingUtils.mapToTagDto(tagRepository.findByTagName(name).orElse(null));
    }

    public Tag getTagByDto(TagDto tagDto) {
        return tagRepository.findById(tagDto.getTagId()).orElseThrow(
                () -> new NotFoundException("Tag " + tagDto.getTagType() + ":" + tagDto.getTagName() + " not found")
        );
    }

    public List<TagDto> getTagsStartingWith(String prefix) {
        return tagRepository.findAllByTagNameStartingWith(prefix)
                .stream().map(MappingUtils::mapToTagDto).toList();
    }

    public void update(TagDto tagDto) {
        Tag tag = tagRepository.findById(tagDto.getTagId()).orElseThrow(
                () -> new NotFoundException("Tag '" + tagDto.getTagType() + ":" + tagDto.getTagName() + "' cannot be updated, because it's not found")
        );
        try {
            tag.setTagType(TagType.valueOf(tagDto.getTagType()));
            tag.setTagName(tagDto.getTagName());
            tagRepository.save(tag);
        } catch (IllegalArgumentException e) {
            throw new BadArgumentException("Bad tag type '" + tagDto.getTagType() + "'");
        }
    }

    public void delete(Long tagId) {
        tagRepository.deleteById(tagId);
    }

    public Set<Tag> tagNamesToTags(String... tagNames) {
        Set<Tag> tags = new HashSet<>();
        for (var name : tagNames) {
            String[] parts = name.split(":", 2);
            tagRepository.findByTagTypeAndTagName(TagType.valueOf(parts[0].toUpperCase()), parts[1])
                    .ifPresent(tags::add);
        }
        return tags;
    }

    public Set<Tag> extractTags(Map<String, String> map) {
        List<String> tagNames = new ArrayList<>();
        for (var val : map.keySet()) {
            if (val.startsWith("tag")) {
                tagNames.add(map.get(val));
            }
        }
        Set<Tag> tags = new HashSet<>();
        for (var name : tagNames) {
            String[] parts = name.split(":", 2);
            Optional<Tag> tagOpt = tagRepository.findByTagTypeAndTagName(TagType.valueOf(parts[0].toUpperCase()), parts[1]);
            if (tagOpt.isEmpty()) {
                Tag newTag = new Tag();
                newTag.setTagName(parts[1].toLowerCase());
                try {
                    newTag.setTagType(TagType.valueOf(parts[0].toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    throw new BadArgumentException("Invalid tag type '" + parts[0] + "'");
                }
                newTag.setUsageCount(1L);
                tags.add(newTag);
            } else {
                Tag tag = tagOpt.get();
                tag.setUsageCount(tag.getUsageCount() + 1);
                tags.add(tag);
            }
        }
        for (var tag : tags) {
            tag = tagRepository.save(tag);
        }
        return tags;
    }

    public void decreaseTag(Tag tag) {
        tag.setUsageCount(tag.getUsageCount() - 1);
        tagRepository.save(tag);
    }


}
