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
@Table(name = "products")
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private Seller seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    private BigDecimal discountPrice;

    @Builder.Default
    @Column(nullable = false)
    private Integer quantity = 0;

    private String sku;

    private String barcode;

    @Builder.Default
    private boolean isFeatured = false;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;

    @Builder.Default
    private boolean isApproved = false;

    private BigDecimal weight;

    private String dimensions;

    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Builder.Default
    private Integer totalReviews = 0;

    @Builder.Default
    private Integer totalSales = 0;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAttribute> attributes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductVariant> variants = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "product")
    private List<ProductReview> reviews = new ArrayList<>();
}