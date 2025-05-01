package com.example.demo.service;

import com.example.demo.entity.Address;
import com.example.demo.entity.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService extends BaseService<Address, Long> {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        super(addressRepository);
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Address> findByUser(Long userId) {
        return userRepository.findById(userId).map(addressRepository::findByUser)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
    }

    public Optional<Address> findDefaultAddress(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return addressRepository.findByUserAndIsDefaultIsTrue(user);
    }

    public List<Address> findByUserAndAddressType(Long userId, Address.AddressType addressType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        return addressRepository.findByUserAndAddressType(user, addressType);
    }

    @Transactional
    public Address save(Long userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        address.setUser(user);

        // Eğer bu adres varsayılan olarak işaretlenmişse, diğer varsayılan adresi kaldır
        if (address.isDefault()) {
            addressRepository.findByUserAndIsDefaultIsTrue(user).ifPresent(existingDefault -> {
                existingDefault.setDefault(false);
                addressRepository.save(existingDefault);
            });
        }

        return addressRepository.save(address);
    }

    @Transactional
    public Address update(Long addressId, Address updatedAddress) {
        return addressRepository.findById(addressId).map(address -> {
            address.setRecipientName(updatedAddress.getRecipientName());
            address.setPhone(updatedAddress.getPhone());
            address.setCountry(updatedAddress.getCountry());
            address.setCity(updatedAddress.getCity());
            address.setDistrict(updatedAddress.getDistrict());
            address.setPostalCode(updatedAddress.getPostalCode());
            address.setAddressLine1(updatedAddress.getAddressLine1());
            address.setAddressLine2(updatedAddress.getAddressLine2());
            address.setAddressType(updatedAddress.getAddressType());

            // Eğer bu adres varsayılan olarak işaretlenmişse, diğer varsayılan adresi kaldır
            if (updatedAddress.isDefault() && !address.isDefault()) {
                addressRepository.findByUserAndIsDefaultIsTrue(address.getUser()).ifPresent(existingDefault -> {
                    existingDefault.setDefault(false);
                    addressRepository.save(existingDefault);
                });
                address.setDefault(true);
            }

            return addressRepository.save(address);
        }).orElseThrow(() -> new RuntimeException("Adres bulunamadı"));
    }

    @Transactional
    public Address setAsDefault(Long addressId) {
        return addressRepository.findById(addressId).map(address -> {
            // Mevcut varsayılan adresi kaldır
            addressRepository.findByUserAndIsDefaultIsTrue(address.getUser()).ifPresent(existingDefault -> {
                existingDefault.setDefault(false);
                addressRepository.save(existingDefault);
            });

            // Bu adresi varsayılan olarak işaretle
            address.setDefault(true);
            return addressRepository.save(address);
        }).orElseThrow(() -> new RuntimeException("Adres bulunamadı"));
    }
}