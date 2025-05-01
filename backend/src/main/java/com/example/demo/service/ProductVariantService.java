package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductVariantRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductVariantService extends BaseService<ProductVariant, Long> {

    private final ProductVariantRepository productVariantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(
            ProductVariantRepository productVariantRepository,
            ProductRepository productRepository
    ) {
        super(productVariantRepository);
        this.productVariantRepository = productVariantRepository;
        this.productRepository = productRepository;
    }

    public List<ProductVariant> findByProductId(Long productId) {
        return productVariantRepository.findByProductId(productId);
    }

    @Transactional
    @Override
    public ProductVariant save(ProductVariant productVariant) {
        // Ürün var mı kontrol et
        Product product = productRepository.findById(productVariant.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        
        productVariant.setProduct(product);
        
        // Ürünün varyant adı benzersiz olmalı
        if (productVariantRepository.existsByProductIdAndVariantName(
                product.getId(), productVariant.getVariantName())) {
            throw new RuntimeException("Bu ürün için aynı isimli varyant zaten mevcut");
        }
        
        // Eğer stok 0'dan küçükse, 0 yap
        if (productVariant.getQuantity() < 0) {
            productVariant.setQuantity(0);
        }
        
        return super.save(productVariant);
    }

    @Transactional
    public ProductVariant update(Long variantId, ProductVariant updatedVariant) {
        return productVariantRepository.findById(variantId)
                .map(variant -> {
                    variant.setVariantName(updatedVariant.getVariantName());
                    variant.setAdditionalPrice(updatedVariant.getAdditionalPrice());
                    variant.setQuantity(updatedVariant.getQuantity());
                    variant.setSku(updatedVariant.getSku());
                    variant.setActive(updatedVariant.isActive());
                    
                    return productVariantRepository.save(variant);
                })
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }

    @Transactional
    public ProductVariant updateStock(Long variantId, Integer quantity) {
        return productVariantRepository.findById(variantId)
                .map(variant -> {
                    variant.setQuantity(quantity);
                    return productVariantRepository.save(variant);
                })
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }

    @Transactional
    public void deactivateVariant(Long variantId) {
        productVariantRepository.findById(variantId)
                .map(variant -> {
                    variant.setActive(false);
                    return productVariantRepository.save(variant);
                })
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }

    @Transactional
    public void activateVariant(Long variantId) {
        productVariantRepository.findById(variantId)
                .map(variant -> {
                    variant.setActive(true);
                    return productVariantRepository.save(variant);
                })
                .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
    }
    
    @Transactional
    public void deleteByProductId(Long productId) {
        productVariantRepository.deleteByProductId(productId);
    }
}
