package com.ecommerceapp.store.repositories;

import com.ecommerceapp.store.entities.Address;
import com.ecommerceapp.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);

    Optional<Address> findByUserAndIsDefaultTrue(User user);
}