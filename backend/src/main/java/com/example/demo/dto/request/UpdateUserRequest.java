package com.example.demo.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(min = 2, max = 50, message = "İsim 2-50 karakter arasında olmalıdır")
    private String firstName;

    @Size(min = 2, max = 50, message = "Soyisim 2-50 karakter arasında olmalıdır")
    private String lastName;

    @Pattern(regexp = "^(0|\\+90)[\\s]?([0-9]{3})[\\s]?([0-9]{3})[\\s]?([0-9]{2})[\\s]?([0-9]{2})$", 
             message = "Geçerli bir Türkiye telefon numarası giriniz")
    private String phone;
    
    private String dateOfBirth; // yyyy-MM-dd formatında
    
    private String profilePicture; // Base64 kodlanmış veya dosya yolu
}