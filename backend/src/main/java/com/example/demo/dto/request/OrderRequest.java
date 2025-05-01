package com.example.demo.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    @NotNull(message = "Teslimat adresi zorunludur")
    @Valid
    private OrderAddressRequest shippingAddress;
    
    @NotNull(message = "Fatura adresi zorunludur")
    @Valid
    private OrderAddressRequest billingAddress;
    
    @NotEmpty(message = "Sipariş kalemleri zorunludur")
    @Valid
    private List<OrderItemRequest> orderItems;
    
    @NotNull(message = "Ödeme yöntemi zorunludur")
    private String paymentMethod; // CREDIT_CARD, BANK_TRANSFER, CASH_ON_DELIVERY
    
    private String couponCode; // İndirim kuponu (opsiyonel)
    
    private String notes; // Sipariş notu (opsiyonel)
}