package com.example.wallpapers.controller;

import com.example.wallpapers.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/api/test")
@RestController
public class TestController {
    private final AuthService authService;

    @Autowired
    public TestController(AuthService authService) {
        this.authService = authService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("hello/user")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello user "+ authService.getAuthentication().getPrincipal() + "!");
    }

    @PreAuthorize("hasRole('ROLE_MODER')")
    @GetMapping("hello/moder")
    public ResponseEntity<String> helloModer() {
        return ResponseEntity.ok("Hello moder "+ authService.getAuthentication().getPrincipal() + "!");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("hello/admin")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Hello admin "+ authService.getAuthentication().getPrincipal() + "!");
    }
}
