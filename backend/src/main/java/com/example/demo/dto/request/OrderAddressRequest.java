package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderAddressRequest {

    @NotBlank(message = "Ad soyad zorunludur")
    private String fullName;
    
    @NotBlank(message = "Telefon numarası zorunludur")
    private String phone;
    
    @NotBlank(message = "Adres satırı 1 zorunludur")
    private String addressLine1;
    
    private String addressLine2;
    
    @NotBlank(message = "Şehir zorunludur")
    private String city;
    
    @NotBlank(message = "İlçe zorunludur")
    private String district;
    
    @NotBlank(message = "Posta kodu zorunludur")
    private String zipCode;
    
    @NotBlank(message = "Ülke zorunludur")
    private String country;
    
    // Fatura adresi için vergi bilgileri (gerekli olabilir)
    private String taxNumber;
    private String taxOffice;
    
    @Size(max = 500, message = "Adres notu en fazla 500 karakter olabilir")
    private String notes;
}