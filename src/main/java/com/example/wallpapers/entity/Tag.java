package com.example.wallpapers.entity;

import com.example.wallpapers.enums.TagType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "tags")
@Entity
public class Tag {
    @Id
    @Column(name = "tag_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Enumerated(EnumType.STRING)
    private TagType tagType;

    @Column(name = "tag_name", length = 255)
    private String tagName;

    @ManyToMany(mappedBy = "postTags", fetch = FetchType.LAZY)
    private Set<Post> posts;

    @Column(name = "usage_count")
    private Long usageCount;
}
