package com.example.wallpapers.repository;

import com.example.wallpapers.entity.Favourite;
import com.example.wallpapers.entity.FavouriteId;
import com.example.wallpapers.entity.Post;
import com.example.wallpapers.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, FavouriteId> {
}
