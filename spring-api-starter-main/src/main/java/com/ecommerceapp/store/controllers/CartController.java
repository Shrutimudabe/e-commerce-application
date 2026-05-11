package com.ecommerceapp.store.controllers;

import com.ecommerceapp.store.dtos.CartRequest;
import com.ecommerceapp.store.dtos.CartResponse;
import com.ecommerceapp.store.entities.CartItem;
import com.ecommerceapp.store.entities.User;
import com.ecommerceapp.store.mappers.CartMapper;
import com.ecommerceapp.store.security.CustomUserDetails;
import com.ecommerceapp.store.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //  1. Add to Cart
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody CartRequest request,
            Authentication authentication
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        CartItem item = cartService.addToCart(
                user,
                request.getProductId(),
                request.getQuantity()
        );

        return ResponseEntity.ok(CartMapper.toResponse(item));
    }

    // 2. Get Cart Items
    @GetMapping
    public ResponseEntity<List<CartResponse>> getCart(Authentication auth) {


        CustomUserDetails userDetails =
                (CustomUserDetails) auth.getPrincipal();
        User user = userDetails.getUser();

        List<CartItem> items = cartService.getCart(user);

        List<CartResponse> response = items.stream()
                .map(CartMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    //  3. Remove Single Item
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long cartItemId,
            Authentication authentication
    ) {
        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();


        cartService.removeItem(cartItemId,user);

        return ResponseEntity.ok("Item removed from cart");
    }

    //  4. Clear Cart
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();


        cartService.clearCart(user);

        return ResponseEntity.ok("Cart cleared successfully");
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<CartResponse> updateQuantity(
            @PathVariable Long cartItemId,
            @RequestParam int quantity,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        CartItem item = cartService.updateQuantity(cartItemId, quantity, user);

        return ResponseEntity.ok(CartMapper.toResponse(item));
    }
}
