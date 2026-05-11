package com.ecommerceapp.store.services.Impl;

import com.ecommerceapp.store.entities.CartItem;
import com.ecommerceapp.store.entities.Product;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.repositories.CartRepository;
import com.ecommerceapp.store.repositories.ProductRepository;
import com.ecommerceapp.store.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem addToCart(User user, Long productId, int quantity) {

        Product product = productRepository.findById(productId).orElseThrow();

        Optional<CartItem> existing = cartRepository.findByUserAndProduct(user, product);

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartRepository.save(item);
        }

        CartItem cartItem = CartItem.builder()
                .user(user)
                .product(product)
                .quantity(quantity)
                .build();

        return cartRepository.save(cartItem);
    }

    @Override
    public List<CartItem> getCart(User user) {
        return cartRepository.findByUserId(user.getId());
    }

    @Override
    public void removeItem(Long cartItemId, User user) {

        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        cartRepository.delete(item);
    }

    @Override
    public void clearCart(User user) {

        List<CartItem> items = cartRepository.findByUserId(user.getId());

        if (!items.isEmpty()) {
            cartRepository.deleteAll(items);
        }
    }

    @Override
    public CartItem updateQuantity(Long cartItemId, int quantity, User user) {

        System.out.println("CartItemId: " + cartItemId);
        System.out.println("Quantity: " + quantity);
        System.out.println("User: " + user);

        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getUser() == null) {
            throw new RuntimeException("Cart item user is null");
        }

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access");
        }

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        item.setQuantity(quantity);

        return cartRepository.save(item);
    }
}
