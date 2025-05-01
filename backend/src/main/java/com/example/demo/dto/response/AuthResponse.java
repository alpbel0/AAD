package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private UserResponse user;
    
    // Aşağıdaki alanları şimdilik siliyoruz çünkü frontend'de kullanılmıyor
    // private String tokenType;
    // private Long expiresIn;
}