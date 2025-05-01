package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private Long variantId;
    private String variantName;
    private Integer quantity;
    private double unitPrice;
    private double totalPrice;
    private LocalDateTime addedAt;
} 