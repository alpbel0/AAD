package com.example.demo.entity;

import com.example.demo.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id")
    private ProductVariant variant;
    
    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;
    
    @Column(name = "added_at")
    private LocalDateTime addedAt;
    
    @PrePersist
    public void prePersist() {
        this.addedAt = LocalDateTime.now();
    }
    
    // Ürün varyantı varsa varyant fiyatını ekleyerek hesapla
    public double getTotalPrice() {
        double basePrice = product.getPrice().doubleValue();
        
        // İndirimli fiyat varsa onu kullan
        if (product.getDiscountPrice() != null) {
            basePrice = product.getDiscountPrice().doubleValue();
        }
        
        // Varyant seçilmişse ek fiyatı ekle
        if (variant != null) {
            basePrice += variant.getAdditionalPrice().doubleValue();
        }
        
        return basePrice * quantity;
    }
} 