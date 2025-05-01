package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductImage;
import com.example.demo.repository.ProductImageRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImageService extends BaseService<ProductImage, Long> {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ProductImageRepository productImageRepository, ProductRepository productRepository) {
        super(productImageRepository);
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    public List<ProductImage> findByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return productImageRepository.findByProductOrderByDisplayOrderAsc(product);
    }

    public Optional<ProductImage> findPrimaryImage(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return productImageRepository.findByProductAndIsPrimaryIsTrue(product);
    }

    @Transactional
    public ProductImage save(Long productId, ProductImage productImage) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        productImage.setProduct(product);

        // Eğer bu görsel ana görsel olarak işaretlenmişse, diğer ana görseli kaldır
        if (productImage.isPrimary()) {
            productImageRepository.findByProductAndIsPrimaryIsTrue(product)
                    .ifPresent(primaryImage -> {
                        primaryImage.setPrimary(false);
                        productImageRepository.save(primaryImage);
                    });
        }

        // Eğer displayOrder belirtilmemişse, en yüksek sirayı bul ve bir fazlasını ata
        if (productImage.getDisplayOrder() == null) {
            List<ProductImage> existingImages = productImageRepository.findByProductOrderByDisplayOrderAsc(product);
            int maxOrder = existingImages.isEmpty() ? 0 :
                    existingImages.get(existingImages.size() - 1).getDisplayOrder();
            productImage.setDisplayOrder(maxOrder + 1);
        }

        return productImageRepository.save(productImage);
    }

    @Transactional
    public ProductImage setPrimary(Long imageId) {
        return productImageRepository.findById(imageId)
                .map(image -> {
                    // Mevcut ana görseli kaldır
                    productImageRepository.findByProductAndIsPrimaryIsTrue(image.getProduct())
                            .ifPresent(primaryImage -> {
                                primaryImage.setPrimary(false);
                                productImageRepository.save(primaryImage);
                            });

                    // Bu görseli ana görsel olarak işaretle
                    image.setPrimary(true);
                    return productImageRepository.save(image);
                })
                .orElseThrow(() -> new RuntimeException("Görsel bulunamadı"));
    }

    @Transactional
    public void updateDisplayOrder(Long productId, List<Long> imageIds) {
        productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        // Her görsel için yeni sıra numarasını güncelle
        for (int i = 0; i < imageIds.size(); i++) {
            Long imageId = imageIds.get(i);
            ProductImage image = productImageRepository.findById(imageId)
                    .orElseThrow(() -> new RuntimeException("Görsel bulunamadı: " + imageId));
            image.setDisplayOrder(i);
            productImageRepository.save(image);
        }
    }
}