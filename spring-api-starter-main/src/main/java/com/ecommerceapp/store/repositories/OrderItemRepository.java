package com.ecommerceapp.store.repositories;

import com.ecommerceapp.store.entities.Order;
import com.ecommerceapp.store.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    //  Get all items of a specific order
    List<OrderItem> findByOrder(Order order);
}
