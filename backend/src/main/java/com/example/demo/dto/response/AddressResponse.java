package com.example.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {

    private Long id;
    private String addressTitle;
    private String recipientName;
    private String phone;
    private String country;
    private String city;
    private String district;
    private String postalCode;
    private String addressLine1;
    private String addressLine2;
    private boolean isDefault;
}