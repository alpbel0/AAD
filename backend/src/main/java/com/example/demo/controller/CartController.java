package com.example.demo.controller;

import com.example.demo.dto.request.AddToCartRequest;
import com.example.demo.dto.request.UpdateCartItemRequest;
import com.example.demo.dto.response.CartItemResponse;
import com.example.demo.dto.response.CartResponse;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.mapper.CartItemMapper;
import com.example.demo.mapper.CartMapper;
import com.example.demo.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;

    /**
     * Kullanıcının sepetini getirir
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        Cart cart = cartService.getUserCart(userId);
        return ResponseEntity.ok(cartMapper.toResponse(cart));
    }

    /**
     * Sepete ürün ekler
     */
    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartItemResponse> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddToCartRequest request) {
        
        Long userId = extractUserId(userDetails);
        CartItem cartItem = cartService.addToCart(
                userId, 
                request.getProductId(), 
                request.getVariantId(), 
                request.getQuantity());
        
        return ResponseEntity.ok(cartItemMapper.toResponse(cartItem));
    }

    /**
     * Sepetten ürün siler
     */
    @DeleteMapping("/{cartItemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId) {
        
        Long userId = extractUserId(userDetails);
        cartService.removeFromCart(userId, cartItemId);
        
        return ResponseEntity.ok().build();
    }

    /**
     * Sepetteki ürün miktarını günceller
     */
    @PutMapping("/{cartItemId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        
        Long userId = extractUserId(userDetails);
        CartItem cartItem = cartService.updateCartItemQuantity(userId, cartItemId, request.getQuantity());
        
        return ResponseEntity.ok(cartItemMapper.toResponse(cartItem));
    }

    /**
     * Sepeti tamamen temizler
     */
    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        cartService.clearCart(userId);
        
        return ResponseEntity.ok().build();
    }

    /**
     * Kullanıcının sepet ürünlerini getirir
     */
    @GetMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<CartItemResponse>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        Long userId = extractUserId(userDetails);
        List<CartItem> cartItems = cartService.getUserCartItems(userId);
        
        List<CartItemResponse> response = cartItems.stream()
                .map(cartItemMapper::toResponse)
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * UserDetails'den kullanıcı ID'sini çıkarır
     */
    private Long extractUserId(UserDetails userDetails) {
        // UserDetails nesnesi kullanarak kullanıcı ID'sini alın
        // Bu, security konfigürasyonunuza bağlı olarak değişebilir
        // Örnek:
        return Long.parseLong(userDetails.getUsername());
    }
} 