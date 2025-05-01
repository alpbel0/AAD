package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileRequest {
    
    @NotBlank(message = "Ad alanı boş olamaz")
    private String firstName;
    
    @NotBlank(message = "Soyad alanı boş olamaz")
    private String lastName;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Telefon numarası 10 haneli olmalıdır")
    private String phone;
    
    private LocalDate dateOfBirth;
    
    private String profilePicture;
}