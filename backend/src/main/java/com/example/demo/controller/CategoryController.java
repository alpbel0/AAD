package com.example.demo.controller;

import com.example.demo.dto.request.CategoryRequest;
import com.example.demo.dto.response.CategoryResponse;
import com.example.demo.entity.Category;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.findAll().stream()
                .map(categoryMapper::toResponse)
                .toList());
    }

    @GetMapping("/active")
    public ResponseEntity<List<CategoryResponse>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.findActiveCategories().stream()
                .map(categoryMapper::toResponse)
                .toList());
    }

    @GetMapping("/main")
    public ResponseEntity<List<CategoryResponse>> getMainCategories() {
        return ResponseEntity.ok(categoryService.findMainCategories().stream()
                .map(categoryMapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        return categoryService.findById(id)
                .map(categoryMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryResponse>> getSubCategories(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findSubCategories(id).stream()
                .map(categoryMapper::toResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        if (request.getParentCategoryId() != null) {
            Category parentCategory = new Category();
            parentCategory.setId(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.save(category)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        Category category = categoryMapper.toEntity(request);
        if (request.getParentCategoryId() != null) {
            Category parentCategory = new Category();
            parentCategory.setId(request.getParentCategoryId());
            category.setParentCategory(parentCategory);
        }
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.update(id, category)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> activateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.activateCategory(id)));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponse> deactivateCategory(@PathVariable Long id) {
        return ResponseEntity.ok(categoryMapper.toResponse(categoryService.deactivateCategory(id)));
    }
}