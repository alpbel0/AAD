package com.example.demo.service;

import com.example.demo.entity.Product;
import com.example.demo.entity.ProductAttribute;
import com.example.demo.repository.ProductAttributeRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductAttributeService extends BaseService<ProductAttribute, Long> {

    private final ProductAttributeRepository productAttributeRepository;
    private final ProductRepository productRepository;

    public ProductAttributeService(
            ProductAttributeRepository productAttributeRepository,
            ProductRepository productRepository
    ) {
        super(productAttributeRepository);
        this.productAttributeRepository = productAttributeRepository;
        this.productRepository = productRepository;
    }

    public List<ProductAttribute> findByProductId(Long productId) {
        return productAttributeRepository.findByProductId(productId);
    }

    @Transactional
    @Override
    public ProductAttribute save(ProductAttribute attribute) {
        // Ürün var mı kontrol et
        Product product = productRepository.findById(attribute.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));
        
        attribute.setProduct(product);
        
        // Öznitelik adı boş olamaz
        if (attribute.getAttributeName() == null || attribute.getAttributeName().trim().isEmpty()) {
            throw new RuntimeException("Öznitelik adı boş olamaz");
        }
        
        // Öznitelik değeri boş olamaz
        if (attribute.getAttributeValue() == null || attribute.getAttributeValue().trim().isEmpty()) {
            throw new RuntimeException("Öznitelik değeri boş olamaz");
        }
        
        // Aynı ürün ve aynı öznitelik adı varsa güncelle
        ProductAttribute existingAttribute = productAttributeRepository
                .findByProductIdAndAttributeName(product.getId(), attribute.getAttributeName())
                .orElse(null);
        
        if (existingAttribute != null) {
            existingAttribute.setAttributeValue(attribute.getAttributeValue());
            return productAttributeRepository.save(existingAttribute);
        }
        
        return super.save(attribute);
    }

    @Transactional
    public ProductAttribute update(Long attributeId, ProductAttribute updatedAttribute) {
        return productAttributeRepository.findById(attributeId)
                .map(attribute -> {
                    attribute.setAttributeName(updatedAttribute.getAttributeName());
                    attribute.setAttributeValue(updatedAttribute.getAttributeValue());
                    
                    return productAttributeRepository.save(attribute);
                })
                .orElseThrow(() -> new RuntimeException("Ürün özniteliği bulunamadı"));
    }
    
    @Transactional
    public void deleteByProductId(Long productId) {
        productAttributeRepository.deleteByProductId(productId);
    }
    
    @Transactional
    @Override
    public List<ProductAttribute> saveAll(List<ProductAttribute> attributes) {
        return attributes.stream()
                .map(this::save)
                .toList();
    }
}
