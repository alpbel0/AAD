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
public class OrderResponse {
    private Long id;
    private String orderNumber;
    private LocalDateTime orderDate;
    private String status;
    private OrderAddressResponse shippingAddress;
    private OrderAddressResponse billingAddress;
    private List<OrderItemResponse> orderItems;
    private Double subtotal;
    private Double shippingCost;
    private Double tax;
    private Double discount;
    private Double total;
    private String paymentMethod;
    private String paymentStatus;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}