package com.example.demo.controller;

import com.example.demo.dto.request.UserProfileRequest;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable Long id) {
        return userService.findById(id)
                .map(userMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserProfileRequest request) {
        User updatedUser = userService.updateProfile(user.getId(), request);
        return ResponseEntity.ok(userMapper.toResponse(updatedUser));
    }

    @PutMapping("/profile/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updatePassword(
            @AuthenticationPrincipal User user,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        userService.updatePassword(user.getId(), currentPassword, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> activateUser(@PathVariable Long id) {
        User user = userService.activateUser(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserProfileResponse> deactivateUser(@PathVariable Long id) {
        User user = userService.deactivateUser(id);
        return ResponseEntity.ok(userMapper.toResponse(user));
    }
}