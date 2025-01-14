package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.UserRequest;
import com.zerobase.fintech.controller.dto.response.UserResponse;
import com.zerobase.fintech.entity.UserEntity;
import com.zerobase.fintech.security.TokenProvider;
import com.zerobase.fintech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    @PostMapping("/register")
    public UserResponse.SignUpResponse register(
            @Valid @RequestBody UserRequest.SignUpRequest request) {
        UserEntity user = userService.register(request);

        return UserResponse.SignUpResponse.of(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody UserRequest.SignInRequest request) {
        var user = userService.authenticate(request);
        var token = tokenProvider.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(token);
    }
}
