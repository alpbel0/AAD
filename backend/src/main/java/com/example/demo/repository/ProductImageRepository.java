package com.example.demo.repository;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends BaseRepository<ProductImage, Long> {
    List<ProductImage> findByProductOrderByDisplayOrderAsc(Product product);
    Optional<ProductImage> findByProductAndIsPrimaryIsTrue(Product product);
}