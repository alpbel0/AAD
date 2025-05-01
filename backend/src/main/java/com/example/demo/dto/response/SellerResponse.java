package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerResponse {
    private Long id;
    private String storeName;
    private String storeDescription;
    private String logoUrl;
    private BigDecimal rating;
    private Integer totalReviews;
}