package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "Ürün varyant ID'si zorunludur")
    private Long productVariantId;
    
    @NotNull(message = "Adet bilgisi zorunludur")
    @Min(value = 1, message = "Adet en az 1 olmalıdır")
    private Integer quantity;
    
    // Sipariş anında kaydedilecek fiyat bilgisi
    // Client tarafından gönderilen fiyat bilgisi sadece referans amaçlıdır
    // Gerçek fiyat sunucu tarafında kontrol edilecektir
    private Double unitPrice;
}