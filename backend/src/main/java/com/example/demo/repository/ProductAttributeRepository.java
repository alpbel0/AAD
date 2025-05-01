package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductAttribute;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductAttributeRepository extends BaseRepository<ProductAttribute, Long> {
    List<ProductAttribute> findByProduct(Product product);
    
    List<ProductAttribute> findByProductId(Long productId);
    
    Optional<ProductAttribute> findByProductIdAndAttributeName(Long productId, String attributeName);
    
    @Modifying
    @Query("DELETE FROM ProductAttribute a WHERE a.product.id = :productId")
    void deleteByProductId(Long productId);
}