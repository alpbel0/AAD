package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductReview;
import com.example.demo.entity.User;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ProductReviewService extends BaseService<ProductReview, Long> {

    private final ProductReviewRepository productReviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ProductReviewService(
            ProductReviewRepository productReviewRepository,
            ProductRepository productRepository,
            UserRepository userRepository
    ) {
        super(productReviewRepository);
        this.productReviewRepository = productReviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Page<ProductReview> findByProductId(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return productReviewRepository.findByProduct(product, pageable);
    }

    public Page<ProductReview> findApprovedByProductId(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        return productReviewRepository.findByProductAndIsApprovedIsTrue(product, pageable);
    }

    public Page<ProductReview> findByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return productReviewRepository.findByUser(user, pageable);
    }

    public boolean hasUserReviewedProduct(Long productId, Long userId, Long orderId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return productReviewRepository.existsByProductAndUserAndOrderId(product, user, orderId);
    }

    @Transactional
    public ProductReview save(Long productId, Long userId, Long orderId, ProductReview review) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // Daha önce inceleme yapılıp yapılmadığını kontrol et
        if (productReviewRepository.existsByProductAndUserAndOrderId(product, user, orderId)) {
            throw new RuntimeException("Bu ürün için zaten bir inceleme yapmışsınız");
        }

        review.setProduct(product);
        review.setUser(user);
        review.setOrderId(orderId);
        review.setApproved(false); // Varsayılan olarak onaylanmamış

        // Kaydet ve ürün puanını güncelle
        ProductReview savedReview = productReviewRepository.save(review);
        updateProductRating(product);
        return savedReview;
    }

    @Transactional
    public ProductReview approveReview(Long reviewId) {
        return productReviewRepository.findById(reviewId)
                .map(review -> {
                    review.setApproved(true);
                    ProductReview savedReview = productReviewRepository.save(review);
                    updateProductRating(review.getProduct());
                    return savedReview;
                })
                .orElseThrow(() -> new RuntimeException("İnceleme bulunamadı"));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        ProductReview review = productReviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("İnceleme bulunamadı"));
        Product product = review.getProduct();
        productReviewRepository.delete(review);
        updateProductRating(product);
    }

    /**
     * Ürünün ortalama puanını ve toplam inceleme sayısını günceller
     */
    private void updateProductRating(Product product) {
        Page<ProductReview> approvedReviews = productReviewRepository.findByProductAndIsApprovedIsTrue(
                product, Pageable.unpaged());
        
        int totalReviews = (int) approvedReviews.getTotalElements();
        product.setTotalReviews(totalReviews);

        if (totalReviews > 0) {
            // Onaylanmış incelemelerin ortalama puanını hesapla
            BigDecimal totalRating = approvedReviews.getContent().stream()
                    .map(review -> BigDecimal.valueOf(review.getRating()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            // Ortalamayı 2 ondalık basamağa yuvarla
            BigDecimal averageRating = totalRating.divide(
                    BigDecimal.valueOf(totalReviews), 2, RoundingMode.HALF_UP);
            
            product.setRating(averageRating);
        } else {
            product.setRating(BigDecimal.ZERO);
        }

        productRepository.save(product);
    }
}