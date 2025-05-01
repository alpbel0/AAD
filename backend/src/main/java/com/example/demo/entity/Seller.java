package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String storeName;

    private String storeDescription;

    private String logoUrl;

    private String bannerUrl;

    @Column(nullable = false)
    private String taxId;

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false)
    private String companyAddress;

    @Column(nullable = false)
    private String contactEmail;

    @Column(nullable = false)
    private String contactPhone;

    @Builder.Default
    @Column(nullable = false)
    private BigDecimal commissionRate = BigDecimal.valueOf(5.00);

    @Builder.Default
    private boolean isApproved = false;

    @Builder.Default
    private boolean isActive = true;

    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    @OneToMany(mappedBy = "seller")
    private List<Product> products = new ArrayList<>();
}