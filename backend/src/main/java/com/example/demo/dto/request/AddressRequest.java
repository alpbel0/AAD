package com.example.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class AddressRequest {

    @NotBlank(message = "Adres başlığı zorunludur")
    @Size(max = 50, message = "Adres başlığı en fazla 50 karakter olabilir")
    private String addressTitle;

    @NotBlank(message = "Alıcı adı zorunludur")
    @Size(max = 100, message = "Alıcı adı en fazla 100 karakter olabilir")
    private String recipientName;

    @NotBlank(message = "Telefon zorunludur")
    @Pattern(regexp = "^(0|\\+90)[\\s]?([0-9]{3})[\\s]?([0-9]{3})[\\s]?([0-9]{2})[\\s]?([0-9]{2})$", 
             message = "Geçerli bir Türkiye telefon numarası giriniz")
    private String phone;

    @NotBlank(message = "Ülke zorunludur")
    private String country;

    @NotBlank(message = "Şehir zorunludur")
    private String city;

    @NotBlank(message = "İlçe/Semt zorunludur")
    private String district;

    @NotBlank(message = "Posta kodu zorunludur")
    private String postalCode;

    @NotBlank(message = "Adres satırı 1 zorunludur")
    @Size(max = 255, message = "Adres satırı 1 en fazla 255 karakter olabilir")
    private String addressLine1;

    @Size(max = 255, message = "Adres satırı 2 en fazla 255 karakter olabilir")
    private String addressLine2;
    
    private boolean isDefault;
}