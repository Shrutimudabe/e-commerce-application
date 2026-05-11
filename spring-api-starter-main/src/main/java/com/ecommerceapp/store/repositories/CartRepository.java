package com.ecommerceapp.store.repositories;

import com.ecommerceapp.store.entities.CartItem;
import com.ecommerceapp.store.entities.Product;
import com.ecommerceapp.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    Optional<CartItem> findByUserAndProduct(User user, Product product);
}