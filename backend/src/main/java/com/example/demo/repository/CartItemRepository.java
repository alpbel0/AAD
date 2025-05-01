package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductVariant;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends BaseRepository<CartItem, Long> {
    
    List<CartItem> findByCart(Cart cart);
    
    List<CartItem> findByCartId(Long cartId);
    
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    
    Optional<CartItem> findByCartAndProductAndVariant(Cart cart, Product product, ProductVariant variant);
    
    void deleteByCartId(Long cartId);
    
    void deleteByCartIdAndProductId(Long cartId, Long productId);
    
    void deleteByCartIdAndProductIdAndVariantId(Long cartId, Long productId, Long variantId);
} 