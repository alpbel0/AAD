package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Integer quantity;
    private String sku;
    private String barcode;
    private boolean isFeatured;
    private boolean isActive;
    private boolean isApproved;
    private BigDecimal weight;
    private String dimensions;
    private BigDecimal rating;
    private Integer totalReviews;
    private Integer totalSales;
    
    // Related entities
    private CategoryResponse category;
    private SellerResponse seller;
    private List<ProductImageResponse> images;
    private List<ProductAttributeResponse> attributes;
    private List<ProductVariantResponse> variants;
    
    // Audit fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}