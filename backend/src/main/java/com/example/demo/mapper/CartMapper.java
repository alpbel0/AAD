package com.example.demo.mapper;

import com.example.demo.dto.response.CartResponse;
import com.example.demo.entity.Cart;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartMapper {
    
    private final CartItemMapper cartItemMapper;
    
    public CartMapper(CartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }
    
    public CartResponse toResponse(Cart cart) {
        if (cart == null) {
            return null;
        }
        
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());
        response.setTotalItems(cart.getTotalItems());
        response.setTotalPrice(cart.getTotalPrice());
        response.setCreatedAt(cart.getCreatedAt());
        response.setUpdatedAt(cart.getUpdatedAt());
        
        // Sepet ürünlerini dönüştür
        if (cart.getCartItems() != null) {
            response.setItems(
                cart.getCartItems().stream()
                    .map(cartItemMapper::toResponse)
                    .collect(Collectors.toList())
            );
        }
        
        return response;
    }
} 