package com.example.demo.controller;

import com.example.demo.dto.request.ProductRequest;
import com.example.demo.dto.response.ProductResponse;
import com.example.demo.mapper.ProductMapper;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findAllActive(pageable).map(productMapper::toResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.findById(id)
                .map(productMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {
        return ResponseEntity.ok(productService.findByCategoryId(categoryId, pageable)
                .map(productMapper::toResponse));
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<Page<ProductResponse>> getProductsBySeller(
            @PathVariable Long sellerId,
            Pageable pageable) {
        return ResponseEntity.ok(productService.findBySellerId(sellerId, pageable)
                .map(productMapper::toResponse));
    }

    @GetMapping("/featured")
    public ResponseEntity<List<ProductResponse>> getFeaturedProducts(Pageable pageable) {
        return ResponseEntity.ok(productService.findFeaturedProducts(pageable).stream()
                .map(productMapper::toResponse)
                .toList());
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam String keyword,
            Pageable pageable) {
        return ResponseEntity.ok(productService.searchProducts(keyword, pageable)
                .map(productMapper::toResponse));
    }

    @GetMapping("/price-range")
    public ResponseEntity<Page<ProductResponse>> getProductsByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(productService.findByPriceRange(minPrice, maxPrice, pageable)
                .map(productMapper::toResponse));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<List<ProductResponse>> getTopRatedProducts() {
        return ResponseEntity.ok(productService.findTopRatedProducts().stream()
                .map(productMapper::toResponse)
                .toList());
    }

    @GetMapping("/best-selling")
    public ResponseEntity<List<ProductResponse>> getBestSellingProducts() {
        return ResponseEntity.ok(productService.findBestSellingProducts().stream()
                .map(productMapper::toResponse)
                .toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productMapper.toResponse(productService.save(productMapper.toEntity(request))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productMapper.toResponse(productService.update(id, productMapper.toEntity(request))));
    }

    @PatchMapping("/{id}/stock")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(productMapper.toResponse(productService.updateStock(id, quantity)));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productService.activateProduct(id)));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productService.deactivateProduct(id)));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> approveProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productMapper.toResponse(productService.approveProduct(id)));
    }
}