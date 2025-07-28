package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.io.CartResponse;

public interface CartService {
    CartResponse addToCart(String foodId);
    CartResponse getCart();
    void clearCart();
    CartResponse removeFromCart(String foodId);
}
