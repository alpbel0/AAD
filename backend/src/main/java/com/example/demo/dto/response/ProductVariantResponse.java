package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantResponse {

    private Long id;
    private String variantName; // Örn. "Renk", "Beden", "Boyut"
    private String variantValue; // Örn. "Kırmızı", "XL", "256GB"
    private BigDecimal priceDifference;
    private BigDecimal finalPrice; // Ana ürünün fiyatı + priceDifference
    private Integer stock;
    private String sku;
    private String image;
    private boolean active;
}