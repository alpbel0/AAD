package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends BaseRepository<ProductVariant, Long> {
    List<ProductVariant> findByProduct(Product product);
    List<ProductVariant> findByProductAndIsActiveIsTrue(Product product);
    List<ProductVariant> findByProductAndQuantityGreaterThan(Product product, Integer quantity);
    List<ProductVariant> findByProductId(Long productId);
    boolean existsByProductIdAndVariantName(Long productId, String variantName);
    
    @Modifying
    @Query("DELETE FROM ProductVariant v WHERE v.product.id = :productId")
    void deleteByProductId(Long productId);
}