package com.example.demo.repository;

import com.example.demo.entity.Address;
import com.example.demo.entity.User;
import com.example.demo.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends BaseRepository<Address, Long> {
    List<Address> findByUser(User user);
    Optional<Address> findByUserAndIsDefaultIsTrue(User user);
    List<Address> findByUserAndAddressType(User user, Address.AddressType addressType);
}