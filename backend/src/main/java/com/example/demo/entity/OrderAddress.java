package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_addresses")
public class OrderAddress extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    private String postalCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private boolean isBillingAddress;

    // Fatura adresi için ek alanlar
    private String companyName;
    private String taxOffice;
    private String taxNumber;
}