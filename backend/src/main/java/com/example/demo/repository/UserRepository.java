package com.example.demo.repository;

import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}