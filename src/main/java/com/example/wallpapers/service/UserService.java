package com.example.wallpapers.service;

import com.example.wallpapers.dto.UserDto;
import com.example.wallpapers.entity.User;
import com.example.wallpapers.enums.Role;
import com.example.wallpapers.enums.UserSort;
import com.example.wallpapers.enums.UserStatus;
import com.example.wallpapers.exception.BadArgumentException;
import com.example.wallpapers.exception.NotFoundException;
import com.example.wallpapers.repository.UserRepository;
import com.example.wallpapers.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final PostService postService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PostService postService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postService = postService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto getUserById(Long userId, Long targetId) {
        User user = userRepository.findById(targetId)
                .orElseThrow(() -> new NotFoundException("User id='" + targetId + "' not found"));
        boolean isOwner = targetId.equals(userId);
        return MappingUtils.mapToUserDto(user, isOwner);
    }

    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User username='" + username + "' not found"));
        return MappingUtils.mapToUserDto(user, true);
    }

    public Page<UserDto> getAllUsers(int page, int size, UserSort sort) {
        Pageable pageable = PageRequest.of(page, size, sort.getSortValue());
        return userRepository.findAll(pageable)
                .map(u -> MappingUtils.mapToUserDto(u, true));
    }

    public void setUserStatus(Long userId, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id='" + userId + "' not found"));
        try {
            UserStatus userStatus = UserStatus.valueOf(UserStatus.class, status);
            if (user.getRoles().contains(Role.ROLE_ADMIN)) {
                throw new BadArgumentException("Cannot change status for user with role ROLE_ADMIN");
            }
            user.setStatus(userStatus);
            userRepository.save(user);
        } catch (IllegalArgumentException | NullPointerException ex) {
            throw new BadArgumentException("Status '" + status + "' is invalid");
        }
    }

    public void changeUserPassword(String oldPassword, String newPassword, Long userId, Long targetId) {
        if (!userId.equals(targetId))
            throw new BadArgumentException("Bad id=" + targetId);
        if (oldPassword.equals(newPassword))
            throw new BadArgumentException("New password should be different from the old one");
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id='" + userId + "' not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

}
