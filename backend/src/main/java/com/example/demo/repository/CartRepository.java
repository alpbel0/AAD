package com.example.demo.repository;

import com.example.demo.entity.Cart;
import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends BaseRepository<Cart, Long> {
    
    Optional<Cart> findByUser(User user);
    
    Optional<Cart> findByUserId(Long userId);
    
    void deleteByUserId(Long userId);
} 