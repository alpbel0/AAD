package com.example.demo.mapper;

import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .imageUrl(category.getImageUrl())
                .isActive(category.isActive())
                .parentCategory(category.getParentCategory() != null ? 
                        toResponse(category.getParentCategory()) : null)
                .subCategories(category.getSubCategories().stream()
                        .map(this::toResponse)
                        .toList())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

    public Category toEntity(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        updateEntity(category, request);
        return category;
    }

    public void updateEntity(Category category, CategoryRequest request) {
        if (request == null) {
            return;
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setImageUrl(request.getImageUrl());
        
        // Parent category will be set by the service
    }
}