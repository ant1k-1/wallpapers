package com.example.wallpapers.repository;

import com.example.wallpapers.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
