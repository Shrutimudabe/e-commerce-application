package com.ecommerceapp.store.services;

import com.ecommerceapp.store.entities.CartItem;
import com.ecommerceapp.store.entities.User;

import java.util.List;


public interface CartService {
    public CartItem addToCart(User user, Long productId, int quantity);
    public List<CartItem> getCart(User user);
    public void removeItem(Long cartItemId,User user);
    public void clearCart(User user);
    public CartItem updateQuantity(Long cartItemId, int quantity, User user);
}
