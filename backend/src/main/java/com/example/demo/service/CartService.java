package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductVariantRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService extends BaseService<Cart, Long> {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository productVariantRepository;
    private final UserService userService;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            ProductVariantRepository productVariantRepository,
            UserService userService
    ) {
        super(cartRepository);
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.productVariantRepository = productVariantRepository;
        this.userService = userService;
    }

    /**
     * Kullanıcının sepetini getirir, yoksa yeni bir sepet oluşturur
     */
    @Transactional
    public Cart getOrCreateCart(Long userId) {
        // Kullanıcıyı kontrol et
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        
        // Sepet varsa getir, yoksa oluştur
        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    /**
     * Sepete ürün ekler, aynı ürün varsa miktarını günceller
     */
    @Transactional
    public CartItem addToCart(Long userId, Long productId, Long variantId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Miktar 0'dan büyük olmalıdır");
        }
        
        Cart cart = getOrCreateCart(userId);
        
        // Ürünü kontrol et
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        
        if (!product.isActive() || !product.isApproved()) {
            throw new RuntimeException("Ürün aktif veya onaylı değil");
        }
        
        // Varyant kontrolü
        ProductVariant variant = null;
        if (variantId != null) {
            variant = productVariantRepository.findById(variantId)
                    .orElseThrow(() -> new RuntimeException("Ürün varyantı bulunamadı"));
            
            if (!variant.getProduct().getId().equals(productId)) {
                throw new RuntimeException("Varyant bu ürüne ait değil");
            }
            
            if (!variant.isActive()) {
                throw new RuntimeException("Ürün varyantı aktif değil");
            }
            
            // Stok kontrolü
            if (variant.getQuantity() < quantity) {
                throw new RuntimeException("Yetersiz stok");
            }
        } else {
            // Ana ürün stok kontrolü
            if (product.getQuantity() < quantity) {
                throw new RuntimeException("Yetersiz stok");
            }
        }
        
        // Sepette aynı ürün ve varyant varsa miktarını güncelle
        Optional<CartItem> existingItem;
        if (variant != null) {
            existingItem = cartItemRepository.findByCartAndProductAndVariant(cart, product, variant);
        } else {
            existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        }
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            // Toplam miktar stok sınırını aşmamalı
            int newQuantity = item.getQuantity() + quantity;
            
            if (variant != null) {
                if (variant.getQuantity() < newQuantity) {
                    throw new RuntimeException("Yetersiz stok");
                }
            } else {
                if (product.getQuantity() < newQuantity) {
                    throw new RuntimeException("Yetersiz stok");
                }
            }
            
            item.setQuantity(newQuantity);
            return cartItemRepository.save(item);
        } else {
            // Yeni sepet ürünü oluştur
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setVariant(variant);
            newItem.setQuantity(quantity);
            
            cart.addCartItem(newItem);
            return cartItemRepository.save(newItem);
        }
    }

    /**
     * Sepetten ürün siler
     */
    @Transactional
    public void removeFromCart(Long userId, Long cartItemId) {
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet ürünü bulunamadı"));
        
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bu sepet ürünü size ait değil");
        }
        
        cart.removeCartItem(cartItem);
        cartItemRepository.delete(cartItem);
    }

    /**
     * Sepetteki ürün miktarını günceller
     */
    @Transactional
    public CartItem updateCartItemQuantity(Long userId, Long cartItemId, Integer quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Miktar 0'dan büyük olmalıdır");
        }
        
        Cart cart = getOrCreateCart(userId);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Sepet ürünü bulunamadı"));
        
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Bu sepet ürünü size ait değil");
        }
        
        // Stok kontrolü
        if (cartItem.getVariant() != null) {
            if (cartItem.getVariant().getQuantity() < quantity) {
                throw new RuntimeException("Yetersiz stok");
            }
        } else {
            if (cartItem.getProduct().getQuantity() < quantity) {
                throw new RuntimeException("Yetersiz stok");
            }
        }
        
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    /**
     * Sepeti tamamen temizler
     */
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        
        // Tüm sepet ürünlerini sil
        cartItemRepository.deleteByCartId(cart.getId());
        
        // Sepeti sıfırla
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    /**
     * Kullanıcının sepetini getirir
     */
    public Cart getUserCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));
    }

    /**
     * Kullanıcının sepet ürünlerini getirir
     */
    public List<CartItem> getUserCartItems(Long userId) {
        Cart cart = getUserCart(userId);
        return cartItemRepository.findByCart(cart);
    }
} 