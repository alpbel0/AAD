package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributeResponse {

    private Long id;
    private String name;  // Örn. "Malzeme", "İşlemci", "Ekran Boyutu"
    private String value; // Örn. "Pamuk", "Intel i7", "15.6 inç"
}