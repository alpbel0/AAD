package com.example.demo.service;

import com.example.demo.dto.request.UserProfileRequest;
import com.example.demo.entity.User;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService extends BaseService<User, Long> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder,
                      UserMapper userMapper) {
        super(userRepository);
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return super.save(user);
    }

    @Transactional
    public User updateProfile(Long userId, UserProfileRequest updatedUserData) {
        return userRepository.findById(userId).map(user -> {
            userMapper.updateEntity(user, updatedUserData);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    @Transactional
    public User updatePassword(Long userId, String currentPassword, String newPassword) {
        return userRepository.findById(userId).map(user -> {
            if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
                throw new RuntimeException("Mevcut şifre yanlış");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    @Transactional
    public User updateLoginTime(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setLastLoginAt(LocalDateTime.now());
            return userRepository.save(user);
        }).orElse(null);
    }

    @Transactional
    public User activateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setActive(true);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    @Transactional
    public User deactivateUser(Long userId) {
        return userRepository.findById(userId).map(user -> {
            user.setActive(false);
            return userRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }
}