package com.chokhaniyash.fooddeliveryappapi.controller;

import com.chokhaniyash.fooddeliveryappapi.io.AuthenticationRequest;
import com.chokhaniyash.fooddeliveryappapi.io.AuthenticationResponse;
import com.chokhaniyash.fooddeliveryappapi.service.AppUserDetailsService;
import com.chokhaniyash.fooddeliveryappapi.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest request){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwtToken = jwtUtil.generateToken(userDetails);
        return AuthenticationResponse.builder().email(request.getEmail()).token(jwtToken).build();
    }

}
