package com.example.wallpapers.repository;

import com.example.wallpapers.entity.Post;
import com.example.wallpapers.entity.Tag;
import com.example.wallpapers.entity.User;
import com.example.wallpapers.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Set;


@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByUser(User user, Pageable pageable);

    @Query("SELECT p " +
            "FROM Favourite f " +
            "JOIN Post p ON f.favouriteId.postId = p.postId " +
            "WHERE f.favouriteId.userId = ?1")
    Page<Post> findFavouritesByUserId(Long userId, Pageable pageable);
    Page<Post> findAllByPostStatus(PostStatus status, Pageable pageable);
    @Query("SELECT DISTINCT p " +
            "FROM Post p " +
            "INNER JOIN p.postTags pt " +
            "INNER JOIN Tag t ON pt.tagId = t.tagId " +
            "WHERE t IN :tags " +
            "GROUP BY p " +
            "HAVING COUNT(DISTINCT t) = :size"
    )
    Page<Post> findAllByPostTagsIn(@Param("tags") Collection<Tag> tags, @Param("size") Integer size, Pageable pageable);
}
