package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService<Category, Long> {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findMainCategories() {
        return categoryRepository.findByParentCategoryIsNull();
    }

    public List<Category> findSubCategories(Long parentCategoryId) {
        return categoryRepository.findById(parentCategoryId)
                .map(parent -> categoryRepository.findByParentCategoryAndIsActiveIsTrue(parent))
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    public List<Category> findActiveCategories() {
        return categoryRepository.findByIsActiveIsTrue();
    }

    @Transactional
    @Override
    public Category save(Category category) {
        // Eğer bir ebeveyn kategorisi ayarlanmışsa, ilişkiyi kontrol et
        if (category.getParentCategory() != null) {
            Long parentId = category.getParentCategory().getId();
            
            // Alt kategori kendisini ebeveyn olarak seçemez
            if (category.getId() != null && parentId.equals(category.getId())) {
                throw new RuntimeException("Kategori kendisini üst kategori olarak seçemez");
            }
            
            // Ebeveyn kategorisini doğrula
            Category parent = categoryRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Üst kategori bulunamadı"));
                    
            category.setParentCategory(parent);
        }
        
        return super.save(category);
    }

    @Transactional
    public Category activateCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setActive(true);
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    @Transactional
    public Category deactivateCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setActive(false);
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    @Transactional
    public Category update(Long categoryId, Category updatedCategory) {
        return categoryRepository.findById(categoryId)
                .map(category -> {
                    category.setName(updatedCategory.getName());
                    category.setDescription(updatedCategory.getDescription());
                    category.setImageUrl(updatedCategory.getImageUrl());
                    
                    // Ebeveyn kategori güncellemesi
                    if (updatedCategory.getParentCategory() != null) {
                        Long parentId = updatedCategory.getParentCategory().getId();
                        
                        // Alt kategori kendisini ebeveyn olarak seçemez
                        if (parentId.equals(categoryId)) {
                            throw new RuntimeException("Kategori kendisini üst kategori olarak seçemez");
                        }
                        
                        Category parent = categoryRepository.findById(parentId)
                                .orElseThrow(() -> new RuntimeException("Üst kategori bulunamadı"));
                        category.setParentCategory(parent);
                    } else {
                        category.setParentCategory(null);
                    }
                    
                    return categoryRepository.save(category);
                })
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }
}