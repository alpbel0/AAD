package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderAddress;
import com.example.demo.repository.OrderAddressRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.base.BaseService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderAddressService extends BaseService<OrderAddress, Long> {

    private final OrderAddressRepository orderAddressRepository;
    private final OrderRepository orderRepository;

    public OrderAddressService(OrderAddressRepository orderAddressRepository, OrderRepository orderRepository) {
        super(orderAddressRepository);
        this.orderAddressRepository = orderAddressRepository;
        this.orderRepository = orderRepository;
    }

    public Optional<OrderAddress> findByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        return orderAddressRepository.findByOrder(order);
    }
    
    public Optional<OrderAddress> findBillingAddressByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        return orderAddressRepository.findByOrderAndIsBillingAddressIsTrue(order);
    }
    
    public Optional<OrderAddress> findShippingAddressByOrderId(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        return orderAddressRepository.findByOrderAndIsBillingAddressIsFalse(order);
    }

    @Transactional
    public OrderAddress saveShippingAddress(Long orderId, OrderAddress shippingAddress) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        
        shippingAddress.setOrder(order);
        shippingAddress.setBillingAddress(false);
        
        return orderAddressRepository.save(shippingAddress);
    }
    
    @Transactional
    public OrderAddress saveBillingAddress(Long orderId, OrderAddress billingAddress) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));
        
        billingAddress.setOrder(order);
        billingAddress.setBillingAddress(true);
        
        return orderAddressRepository.save(billingAddress);
    }
    
    @Transactional
    public OrderAddress updateAddress(Long addressId, OrderAddress updatedAddress) {
        return orderAddressRepository.findById(addressId)
                .map(address -> {
                    address.setRecipientName(updatedAddress.getRecipientName());
                    address.setPhone(updatedAddress.getPhone());
                    address.setCountry(updatedAddress.getCountry());
                    address.setCity(updatedAddress.getCity());
                    address.setDistrict(updatedAddress.getDistrict());
                    address.setPostalCode(updatedAddress.getPostalCode());
                    address.setAddressLine1(updatedAddress.getAddressLine1());
                    address.setAddressLine2(updatedAddress.getAddressLine2());
                    
                    // Fatura adresi için ek bilgiler
                    if (address.isBillingAddress()) {
                        address.setCompanyName(updatedAddress.getCompanyName());
                        address.setTaxOffice(updatedAddress.getTaxOffice());
                        address.setTaxNumber(updatedAddress.getTaxNumber());
                    }
                    
                    return orderAddressRepository.save(address);
                })
                .orElseThrow(() -> new RuntimeException("Adres bulunamadı"));
    }
}