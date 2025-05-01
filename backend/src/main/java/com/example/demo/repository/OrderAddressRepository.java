package com.example.demo.repository;

import com.example.demo.entity.Order;
import com.example.demo.entity.OrderAddress;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderAddressRepository extends BaseRepository<OrderAddress, Long> {
    Optional<OrderAddress> findByOrder(Order order);
    Optional<OrderAddress> findByOrderAndIsBillingAddressIsTrue(Order order);
    Optional<OrderAddress> findByOrderAndIsBillingAddressIsFalse(Order order);
}