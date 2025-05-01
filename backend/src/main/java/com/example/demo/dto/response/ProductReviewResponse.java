package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductReviewResponse {

    private Long id;
    private Long productId;
    private String productName;
    private UserResponse user;
    private Integer rating;
    private String comment;
    private List<String> images;
    private boolean verified; // Doğrulanmış satın alma
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}