package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeRequest {

    @NotBlank(message = "Özellik adı zorunludur")
    private String name;  // Örn. "Malzeme", "İşlemci", "Ekran Boyutu"
    
    @NotBlank(message = "Özellik değeri zorunludur")
    private String value; // Örn. "Pamuk", "Intel i7", "15.6 inç"
}