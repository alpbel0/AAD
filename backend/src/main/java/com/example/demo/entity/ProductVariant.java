package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product_variants")
public class ProductVariant extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String variantName;

    @Builder.Default
    private BigDecimal additionalPrice = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantity = 0;

    private String sku;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;
}