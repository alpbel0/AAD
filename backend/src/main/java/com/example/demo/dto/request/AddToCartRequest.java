package com.example.demo.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {
    
    @NotNull(message = "Ürün ID boş olamaz")
    private Long productId;
    
    // Varyant seçimi opsiyonel olabilir
    private Long variantId;
    
    @NotNull(message = "Miktar boş olamaz")
    @Min(value = 1, message = "Miktar en az 1 olmalıdır")
    private Integer quantity = 1;
} 