package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.controller.CartController;
import com.chokhaniyash.fooddeliveryappapi.entity.CartEntity;
import com.chokhaniyash.fooddeliveryappapi.io.CartResponse;
import com.chokhaniyash.fooddeliveryappapi.repository.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final UserService userService;

    @Override
    public CartResponse addToCart(String foodId) {
        String loggedInUserId = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(loggedInUserId);
        CartEntity cart = cartOptional.orElseGet(() -> new CartEntity(loggedInUserId,new HashMap<>()));
        Map<String,Integer> cartItems = cart.getItems();
        cartItems.put(foodId,cartItems.getOrDefault(foodId,0)+1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String id = userService.findByUserId();
        Optional<CartEntity> cartOptional = cartRepository.findByUserId(id);
        CartEntity cart = cartOptional.orElse( new CartEntity(null,id,new HashMap<>()) );
        return convertToResponse(cart);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(String foodId) {
        String loggedInUser = userService.findByUserId();
        CartEntity cart =  cartRepository.findByUserId(loggedInUser).orElseThrow(() -> new RuntimeException("Cart is not found"));
        Map<String,Integer> items = cart.getItems();
        if(items.containsKey(foodId)){
            items.put(foodId,items.get(foodId) - 1);
            if(items.get(foodId) == 0)
                items.remove(foodId);
        }
        cart = cartRepository.save(cart);
        return convertToResponse(cart);
    }

    private CartResponse convertToResponse(CartEntity cart){
        return CartResponse.builder()
                .id(cart.getId())
                .items(cart.getItems())
                .userId(cart.getUserId())
                .build();
    }

}
