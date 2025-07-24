package com.chokhaniyash.fooddeliveryappapi.service;

import com.chokhaniyash.fooddeliveryappapi.entity.UserEntity;
import com.chokhaniyash.fooddeliveryappapi.io.UserRequest;
import com.chokhaniyash.fooddeliveryappapi.io.UserResponse;
import com.chokhaniyash.fooddeliveryappapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse registerUser(UserRequest request) {
        UserEntity newUser = convertToEntity(request);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }
    private UserEntity convertToEntity(UserRequest request){
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
    }
    private UserResponse convertToResponse(UserEntity user){
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
