package com.example.wallpapers.repository;

import com.example.wallpapers.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
    
}
