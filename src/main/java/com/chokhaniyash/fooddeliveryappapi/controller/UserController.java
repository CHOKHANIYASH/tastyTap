package com.chokhaniyash.fooddeliveryappapi.controller;

import com.chokhaniyash.fooddeliveryappapi.io.UserRequest;
import com.chokhaniyash.fooddeliveryappapi.io.UserResponse;
import com.chokhaniyash.fooddeliveryappapi.service.FoodService;
import com.chokhaniyash.fooddeliveryappapi.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@RequestBody UserRequest request){
        return userService.registerUser(request);
    }
}
