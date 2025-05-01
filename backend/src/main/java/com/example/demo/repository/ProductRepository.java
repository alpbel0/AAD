package com.example.demo.repository;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Seller;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends BaseRepository<Product, Long> {
    Page<Product> findByIsActiveIsTrue(Pageable pageable);
    
    Page<Product> findByCategoryAndIsActiveIsTrue(Category category, Pageable pageable);
    
    Page<Product> findBySellerAndIsActiveIsTrue(Seller seller, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.isFeatured = true")
    List<Product> findFeaturedProducts(Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.price BETWEEN :minPrice AND :maxPrice")
    Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND " +
           "(LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchProducts(String keyword, Pageable pageable);
    
    List<Product> findTop10ByOrderByRatingDesc();
    
    List<Product> findTop10ByOrderByTotalSalesDesc();
}