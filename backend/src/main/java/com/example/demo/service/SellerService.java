package com.example.demo.service;

import com.example.demo.entity.Seller;
import com.example.demo.entity.User;
import com.example.demo.repository.SellerRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class SellerService extends BaseService<Seller, Long> {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerService(SellerRepository sellerRepository, UserRepository userRepository) {
        super(sellerRepository);
        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    public Optional<Seller> findByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return sellerRepository.findByUser(user);
    }

    public Page<Seller> findAllActive(Pageable pageable) {
        return sellerRepository.findByIsActiveIsTrue(pageable);
    }

    public Page<Seller> findAllApprovedAndActive(Pageable pageable) {
        return sellerRepository.findByIsApprovedIsTrueAndIsActiveIsTrue(pageable);
    }

    public List<Seller> findTopSellers() {
        return sellerRepository.findTop10ByIsActiveIsTrueOrderByRatingDesc();
    }

    @Transactional
    public Seller register(Long userId, Seller sellerData) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        
        // Kullanıcının zaten satıcı olup olmadığını kontrol et
        if (sellerRepository.findByUser(user).isPresent()) {
            throw new RuntimeException("Bu kullanıcı zaten bir satıcı hesabına sahip");
        }
        
        // User tipi güncelleniyor
        user.setUserType(User.UserType.SELLER);
        userRepository.save(user);
        
        // Yeni satıcı kaydı oluşturuluyor
        sellerData.setUser(user);
        sellerData.setApproved(false); // Varsayılan olarak onaylanmamış
        sellerData.setActive(true); // Aktif olarak başlatılıyor
        sellerData.setRating(BigDecimal.ZERO);
        sellerData.setTotalReviews(0);
        
        return sellerRepository.save(sellerData);
    }

    @Transactional
    public Seller update(Long sellerId, Seller updatedSeller) {
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setStoreName(updatedSeller.getStoreName());
                    seller.setStoreDescription(updatedSeller.getStoreDescription());
                    seller.setLogoUrl(updatedSeller.getLogoUrl());
                    seller.setBannerUrl(updatedSeller.getBannerUrl());
                    seller.setContactEmail(updatedSeller.getContactEmail());
                    seller.setContactPhone(updatedSeller.getContactPhone());
                    // Vergi kimlik numarası, şirket adı gibi hassas bilgiler farklı bir onay süreciyle değiştirilmeli
                    
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    @Transactional
    public Seller approveSeller(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setApproved(true);
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    @Transactional
    public Seller activateSeller(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setActive(true);
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    @Transactional
    public Seller deactivateSeller(Long sellerId) {
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setActive(false);
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    @Transactional
    public Seller updateCommissionRate(Long sellerId, BigDecimal commissionRate) {
        if (commissionRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Komisyon oranı negatif olamaz");
        }
        
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setCommissionRate(commissionRate);
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }

    @Transactional
    public Seller updateSellerRating(Long sellerId, BigDecimal rating, Integer totalReviews) {
        return sellerRepository.findById(sellerId)
                .map(seller -> {
                    seller.setRating(rating);
                    seller.setTotalReviews(totalReviews);
                    return sellerRepository.save(seller);
                })
                .orElseThrow(() -> new RuntimeException("Satıcı bulunamadı"));
    }
}