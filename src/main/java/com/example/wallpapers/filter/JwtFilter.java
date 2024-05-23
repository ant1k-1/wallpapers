package com.example.wallpapers.filter;

import com.example.wallpapers.enums.AuthStatus;
import com.example.wallpapers.exception.AuthException;
import com.example.wallpapers.exception.CustomExceptionHandler;
import com.example.wallpapers.exception.ErrorMessage;
import com.example.wallpapers.jwt.JwtAuthentication;
import com.example.wallpapers.service.AuthService;
import com.example.wallpapers.utils.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;


@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {
    private static final String AUTHORIZATION = "Authorization";

    private final AuthService authService;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc)
            throws IOException, ServletException {
        final String token = getTokenFromRequest((HttpServletRequest) request);
        if (token != null) {
            AuthStatus status = authService.validateToken(token);
            if (status == AuthStatus.TOKEN_VALID) {
                final Claims claims = authService.getClaims(token);
                final JwtAuthentication jwtInfoToken = JwtUtils.generate(claims);
                jwtInfoToken.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(jwtInfoToken);
            } else {
                authenticationEntryPoint.commence(
                        (HttpServletRequest) request,
                        (HttpServletResponse) response,
                        new AuthException(
                                status,
                                HttpStatus.UNAUTHORIZED
                        )
                );
                return;
            }
        } else {
            authenticationEntryPoint.commence(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    new AuthException(
                            AuthStatus.TOKEN_MISSING,
                            HttpStatus.UNAUTHORIZED
                    )
            );
            return;
        }

        fc.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearer = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(bearer) && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }
}
