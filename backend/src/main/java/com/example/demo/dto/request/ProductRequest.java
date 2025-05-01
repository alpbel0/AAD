package com.example.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Ürün adı boş olamaz")
    @Size(min = 3, max = 200, message = "Ürün adı 3-200 karakter arasında olmalıdır")
    private String name;

    @NotBlank(message = "Ürün açıklaması boş olamaz")
    @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
    private String description;
    
    @NotNull(message = "Fiyat boş olamaz")
    @Min(value = 0, message = "Fiyat 0'dan küçük olamaz")
    private BigDecimal price;

    private BigDecimal discountPrice;
    
    @NotNull(message = "Stok miktarı zorunludur")
    @Min(value = 0, message = "Stok miktarı negatif olamaz")
    private Integer stock;
    
    @NotNull(message = "Kategori ID boş olamaz")
    private Long categoryId;
    
    @NotNull(message = "Satıcı ID zorunludur")
    private Long sellerId;
    
    private String brand;
    private String model;
    private BigDecimal discountRate;
    private String sku; // Stock Keeping Unit
    private String barcode;
    private boolean isFeatured;
    private BigDecimal weight;
    private String dimensions;
    
    // Ürün varyantları
    private List<ProductVariantRequest> variants;
    
    // Ürün özellikleri/nitelikleri
    private List<ProductAttributeRequest> attributes;
    
    // Ürün görselleri (Base64 kodlanmış veya dosya yolu)
    @NotEmpty(message = "En az bir ürün görseli zorunludur")
    private List<String> images;
}