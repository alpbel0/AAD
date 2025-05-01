package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantRequest {

    @NotBlank(message = "Varyant adı zorunludur")
    private String variantName; // Örn. "Renk", "Beden", "Boyut"
    
    @NotBlank(message = "Varyant değeri zorunludur")
    private String variantValue; // Örn. "Kırmızı", "XL", "256GB"
    
    // Varyanta özel fiyat farkı (artı veya eksi olabilir)
    private BigDecimal priceDifference;
    
    // Varyanta özel stok miktarı
    @Min(value = 0, message = "Stok miktarı negatif olamaz")
    private Integer stock;
    
    // Varyant için SKU (Stock Keeping Unit)
    private String sku;
    
    // Varyanta özel görsel (opsiyonel)
    private String image;
}