package com.example.demo.mapper;

import com.example.demo.dto.request.UserProfileRequest;
import com.example.demo.dto.response.UserProfileResponse;
import com.example.demo.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileResponse toResponse(User user) {
        if (user == null) {
            return null;
        }

        return UserProfileResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .dateOfBirth(user.getDateOfBirth())
                .profilePicture(user.getProfilePicture())
                .userType(user.getUserType())
                .isActive(user.isActive())
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public void updateEntity(User user, UserProfileRequest request) {
        if (request == null) {
            return;
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setProfilePicture(request.getProfilePicture());
    }
}