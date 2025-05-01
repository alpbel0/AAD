package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private Long id;
    private Long userId;
    private int totalItems;
    private double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<CartItemResponse> items = new ArrayList<>();
} 