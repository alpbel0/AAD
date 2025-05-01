package com.example.demo.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewRequest {

    @NotNull(message = "Ürün ID'si zorunludur")
    private Long productId;
    
    @NotNull(message = "Değerlendirme puanı zorunludur")
    @Min(value = 1, message = "Değerlendirme puanı en az 1 olmalıdır")
    @Max(value = 5, message = "Değerlendirme puanı en fazla 5 olmalıdır")
    private Integer rating;
    
    @Size(max = 1000, message = "Yorum en fazla 1000 karakter olabilir")
    private String comment;
    
    // Değerlendirmeye fotoğraf eklemek için (opsiyonel)
    private String[] images;
}