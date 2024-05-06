package com.example.wallpapers.utils;

import com.example.wallpapers.enums.Role;
import com.example.wallpapers.enums.UserStatus;
import com.example.wallpapers.jwt.JwtAuthentication;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtUtils {
    public static JwtAuthentication generate(Claims claims) {
        final JwtAuthentication jwtInfoToken = new JwtAuthentication();
        jwtInfoToken.setRoles(getRoles(claims));
        jwtInfoToken.setUsername(claims.getSubject());
        jwtInfoToken.setId(claims.get("user_id", Long.class));
        jwtInfoToken.setStatus(UserStatus.valueOf(claims.get("status", String.class)));
        return jwtInfoToken;
    }

    private static Set<Role> getRoles(Claims claims) {
        final List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }
}
