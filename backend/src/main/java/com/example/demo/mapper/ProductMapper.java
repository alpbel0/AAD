package com.example.demo.mapper;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.*;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product) {
        if (product == null) {
            return null;
        }

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .quantity(product.getQuantity())
                .sku(product.getSku())
                .barcode(product.getBarcode())
                .isFeatured(product.isFeatured())
                .isActive(product.isActive())
                .isApproved(product.isApproved())
                .weight(product.getWeight())
                .dimensions(product.getDimensions())
                .rating(product.getRating())
                .totalReviews(product.getTotalReviews())
                .totalSales(product.getTotalSales())
                .category(product.getCategory() != null ? CategoryResponse.builder()
                    .id(product.getCategory().getId())
                    .name(product.getCategory().getName())
                    .description(product.getCategory().getDescription())
                    .imageUrl(product.getCategory().getImageUrl())
                    .build() : null)
                .seller(product.getSeller() != null ? SellerResponse.builder()
                    .id(product.getSeller().getId())
                    .storeName(product.getSeller().getStoreName())
                    .storeDescription(product.getSeller().getStoreDescription())
                    .logoUrl(product.getSeller().getLogoUrl())
                    .rating(product.getSeller().getRating())
                    .totalReviews(product.getSeller().getTotalReviews())
                    .build() : null)
                .images(product.getImages().stream()
                        .map(img -> ProductImageResponse.builder()
                            .id(img.getId())
                            .imageUrl(img.getImageUrl())
                            .isPrimary(img.isPrimary())
                            .displayOrder(img.getDisplayOrder())
                            .build())
                        .toList())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }

    public Product toEntity(ProductRequest request) {
        if (request == null) {
            return null;
        }

        Product product = new Product();
        updateEntity(product, request);
        return product;
    }

    public void updateEntity(Product product, ProductRequest request) {
        if (request == null) {
            return;
        }

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setDiscountPrice(request.getDiscountPrice());
        product.setQuantity(request.getStock());
        product.setSku(request.getSku());
        product.setBarcode(request.getBarcode());
        product.setFeatured(request.isFeatured());
        product.setWeight(request.getWeight());
        product.setDimensions(request.getDimensions());
        
        if (request.getCategoryId() != null) {
            Category category = new Category();
            category.setId(request.getCategoryId());
            product.setCategory(category);
        }
    }
}