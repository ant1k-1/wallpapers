package com.example.wallpapers.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "favourites")
public class Favourite {

    @EmbeddedId
    private FavouriteId favouriteId;

    @Column(name = "added_date")
    private LocalDateTime addedDate;

    public Favourite(User user, Post post) {
        favouriteId = new FavouriteId();
        favouriteId.setUserId(user.getUserId());
        favouriteId.setPostId(post.getPostId());
        addedDate = LocalDateTime.now();
    }

//    @Embeddable
//    public static class FavouriteId
}
