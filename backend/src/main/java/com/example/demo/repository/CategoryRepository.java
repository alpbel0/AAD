package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull();
    List<Category> findByParentCategoryAndIsActiveIsTrue(Category parentCategory);
    List<Category> findByIsActiveIsTrue();
}