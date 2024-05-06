package com.example.wallpapers.entity;

import com.example.wallpapers.enums.PostStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "posts")
@Entity
public class Post {
    @Id
    @Column(name = "post_id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "source")
    private String source;

    @Column(name = "dimensions")
    private String dimensions;

    @Column(name = "size")
    private Long size;

    @Column(name = "post_status", length = 32)
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "post_tag",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> postTags;

    @Column(name = "views")
    private Long views;

    @Column(name = "downloads")
    private Long downloads;

    @Column(name = "likes")
    private Long likes;

    @Column(name = "image_uuid")
    private String imageUUID;

    @Column(name = "preview_uuid")
    private String previewUUID;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

}
