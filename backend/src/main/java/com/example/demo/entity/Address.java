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
@Table(name = "addresses")
public class Address extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AddressType addressType;

    @Column(nullable = false)
    private String recipientName;

    @Column(nullable = false)
    private String phone;

    @Builder.Default
    @Column(nullable = false)
    private String country = "Türkiye";

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    private String postalCode;

    @Column(nullable = false)
    private String addressLine1;

    private String addressLine2;

    @Builder.Default
    private boolean isDefault = false;

    public enum AddressType {
        BILLING, SHIPPING
    }
}