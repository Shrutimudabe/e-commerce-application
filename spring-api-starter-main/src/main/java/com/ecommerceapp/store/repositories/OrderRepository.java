package com.ecommerceapp.store.repositories;

import com.ecommerceapp.store.entities.Order;
import com.ecommerceapp.store.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Get all orders of a user (order history)
    List<Order> findByUser(User user);

    // latest orders first
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
