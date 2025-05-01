package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductReview;
import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductReviewRepository extends BaseRepository<ProductReview, Long> {
    Page<ProductReview> findByProduct(Product product, Pageable pageable);
    
    Page<ProductReview> findByProductAndIsApprovedIsTrue(Product product, Pageable pageable);
    
    Page<ProductReview> findByUser(User user, Pageable pageable);
    
    boolean existsByProductAndUserAndOrderId(Product product, User user, Long orderId);
}