package com.example.demo.service;

import com.example.demo.dto.request.LoginRequest;
import com.example.demo.dto.request.RegisterUserRequest;
import com.example.demo.dto.response.AuthResponse;
import com.example.demo.dto.response.UserResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.AuthenticationException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterUserRequest request) {
        log.info("Registering new user with email: {}", request.getEmail());
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already in use: {}", request.getEmail());
            throw new RuntimeException("Bu email adresi zaten kullanımda");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userType(User.UserType.CUSTOMER)
                .isActive(true)
                .build();

        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getEmail());
        
        String token = jwtService.generateToken(user);
        return createAuthResponse(user, token);
    }

    public AuthResponse login(LoginRequest request) {
        log.info("Processing login for email: {}", request.getEmail());
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );
            
            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
            
            if (!user.isActive()) {
                log.warn("Inactive user tried to login: {}", user.getEmail());
                throw new RuntimeException("Hesabınız aktif değil. Lütfen yönetici ile iletişime geçin.");
            }
            
            log.info("User logged in successfully: {}", user.getEmail());
            String token = jwtService.generateToken(user);
            return createAuthResponse(user, token);
            
        } catch (BadCredentialsException e) {
            log.warn("Login failed for email {}: Bad credentials", request.getEmail());
            throw new RuntimeException("Email veya şifre hatalı");
        } catch (AuthenticationException e) {
            log.warn("Login failed for email {}: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Giriş yapılırken bir hata oluştu: " + e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error during login for email {}: {}", request.getEmail(), e.getMessage());
            throw new RuntimeException("Beklenmeyen bir hata oluştu. Lütfen daha sonra tekrar deneyin.");
        }
    }

    private AuthResponse createAuthResponse(User user, String token) {
        return AuthResponse.builder()
                .token(token)
                .user(UserResponse.builder()
                        .id(user.getId())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .role(user.getUserType().name())
                        .build())
                .build();
    }
}