package com.example.demo.mapper;

import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.entity.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    
    public CartItemResponse toResponse(CartItem cartItem) {
        if (cartItem == null) {
            return null;
        }
        
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setProductId(cartItem.getProduct().getId());
        response.setProductName(cartItem.getProduct().getName());
        
        // Ürün resmi (ana resim veya ilk resim)
        if (cartItem.getProduct().getImages() != null && !cartItem.getProduct().getImages().isEmpty()) {
            // Ürün resminin URL'sini al
            response.setProductImage(cartItem.getProduct().getImages().get(0).getImageUrl());
        }
        
        // Varyant bilgileri
        if (cartItem.getVariant() != null) {
            response.setVariantId(cartItem.getVariant().getId());
            response.setVariantName(cartItem.getVariant().getVariantName());
        }
        
        response.setQuantity(cartItem.getQuantity());
        response.setUnitPrice(
            cartItem.getVariant() != null 
                ? cartItem.getProduct().getPrice().doubleValue() + cartItem.getVariant().getAdditionalPrice().doubleValue() 
                : cartItem.getProduct().getPrice().doubleValue()
        );
        response.setTotalPrice(cartItem.getTotalPrice());
        response.setAddedAt(cartItem.getAddedAt());
        
        return response;
    }
} 