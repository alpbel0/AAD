package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.Seller;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SellerRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService extends BaseService<Product, Long> {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            SellerRepository sellerRepository
    ) {
        super(productRepository);
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
    }

    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findByIsActiveIsTrue(pageable);
    }

    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        return categoryRepository.findById(categoryId)
                .map(category -> productRepository.findByCategoryAndIsActiveIsTrue(category, pageable))
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
    }

    public Page<Product> findBySellerId(Long sellerId, Pageable pageable) {
        return sellerRepository.findById(sellerId)
                .map(seller -> productRepository.findBySellerAndIsActiveIsTrue(seller, pageable))
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    public List<Product> findFeaturedProducts(Pageable pageable) {
        return productRepository.findFeaturedProducts(pageable);
    }

    public Page<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository.findByPriceRange(minPrice, maxPrice, pageable);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.searchProducts(keyword, pageable);
    }

    public List<Product> findTopRatedProducts() {
        return productRepository.findTop10ByOrderByRatingDesc();
    }

    public List<Product> findBestSellingProducts() {
        return productRepository.findTop10ByOrderByTotalSalesDesc();
    }

    @Transactional
    @Override
    public Product save(Product product) {
        validateProduct(product);
        // SEO-friendly URL slug oluşturma (başka bir servisten alınabilir)
        return super.save(product);
    }

    @Transactional
    public Product update(Long productId, Product updatedProduct) {
        return productRepository.findById(productId)
                .map(product -> {
                    validateProduct(updatedProduct);

                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setDiscountPrice(updatedProduct.getDiscountPrice());
                    product.setQuantity(updatedProduct.getQuantity());
                    product.setSku(updatedProduct.getSku());
                    product.setBarcode(updatedProduct.getBarcode());
                    product.setFeatured(updatedProduct.isFeatured());
                    product.setWeight(updatedProduct.getWeight());
                    product.setDimensions(updatedProduct.getDimensions());
                    
                    // Eğer kategori değiştiyse, yeni kategoriyi ayarla
                    if (updatedProduct.getCategory() != null && 
                        (product.getCategory() == null || !product.getCategory().getId().equals(updatedProduct.getCategory().getId()))) {
                        Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
                        product.setCategory(category);
                    }

                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    @Transactional
    public Product activateProduct(Long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setActive(true);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    @Transactional
    public Product deactivateProduct(Long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setActive(false);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    @Transactional
    public Product approveProduct(Long productId) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setApproved(true);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    @Transactional
    public Product updateStock(Long productId, Integer quantity) {
        return productRepository.findById(productId)
                .map(product -> {
                    product.setQuantity(quantity);
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
    }

    private void validateProduct(Product product) {
        // Kategori kontrolü
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Kategori bulunamadı"));
        product.setCategory(category);
        
        // Satıcı kontrolü
        Seller seller = sellerRepository.findById(product.getSeller().getId())
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
        product.setSeller(seller);
        
        // Fiyat kontrolü
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Ürün fiyatı sıfırdan büyük olmalıdır");
        }
        
        // İndirimli fiyat kontrolü
        if (product.getDiscountPrice() != null && 
            product.getDiscountPrice().compareTo(product.getPrice()) >= 0) {
            throw new RuntimeException("İndirimli fiyat normal fiyattan düşük olmalıdır");
        }
    }
}