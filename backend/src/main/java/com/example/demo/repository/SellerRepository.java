package com.example.demo.repository;

import com.example.demo.entity.Seller;
import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepository extends BaseRepository<Seller, Long> {
    Optional<Seller> findByUser(User user);
    
    Page<Seller> findByIsActiveIsTrue(Pageable pageable);
    
    Page<Seller> findByIsApprovedIsTrueAndIsActiveIsTrue(Pageable pageable);
    
    List<Seller> findTop10ByIsActiveIsTrueOrderByRatingDesc();
}