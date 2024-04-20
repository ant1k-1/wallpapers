package com.example.wallpapers.model;

import com.example.wallpapers.enums.Status;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Status postStatus;

    @ManyToMany
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

    @Column(name = "rating")
    private Long rating;

    @Column(name = "image_uuid")
    private String imageUUID;

    @Column(name = "preview_uuid")
    private String previewUUID;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
}
