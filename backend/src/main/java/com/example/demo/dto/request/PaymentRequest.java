package com.example.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private String paymentMethod;
    private String paymentProvider;
    private String transactionId;
    private Double amount;
    private String currency;
    private Boolean isSuccessful;
}