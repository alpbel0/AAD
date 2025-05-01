package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import com.example.demo.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal totalAmount;

    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(nullable = false)
    private BigDecimal finalAmount;

    @Column(nullable = false)
    private BigDecimal shippingCost;

    private String couponCode;

    private BigDecimal couponDiscount;

    private String note;

    private LocalDateTime paidAt;

    private LocalDateTime processedAt;

    private LocalDateTime shippedAt;

    private LocalDateTime deliveredAt;

    private LocalDateTime cancelledAt;

    private String cancellationReason;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private OrderAddress orderAddress;

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
    }

    public void setOrderAddress(OrderAddress address) {
        if (address == null) {
            if (this.orderAddress != null) {
                this.orderAddress.setOrder(null);
            }
        } else {
            address.setOrder(this);
        }
        this.orderAddress = address;
    }
}