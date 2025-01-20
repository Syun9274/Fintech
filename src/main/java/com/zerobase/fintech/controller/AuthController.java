package com.zerobase.fintech.controller;

import com.zerobase.fintech.common.enums.UserRole;
import com.zerobase.fintech.controller.dto.request.UserRequest;
import com.zerobase.fintech.controller.dto.response.UserResponse;
import com.zerobase.fintech.entity.UserEntity;
import com.zerobase.fintech.security.TokenProvider;
import com.zerobase.fintech.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    private final UserService userService;
    private final TokenProvider tokenProvider;

    // 일반 사용자
    @PostMapping("/register")
    public UserResponse.SignUpResponse register(
            @Valid @RequestBody UserRequest.SignUpRequest request
    ) {
        UserEntity user = userService.register(request, UserRole.ROLE_USER);

        return UserResponse.SignUpResponse.of(user);
    }

    // 관리자
    // 관계자만 접근 가능하도록 특수한 URL 사용
    @PostMapping("/YWRtaW4K")
    public UserResponse.SignUpResponse adminRegister(
            @Valid @RequestBody UserRequest.SignUpRequest request
    ) {
        UserEntity user = userService.register(request, UserRole.ROLE_ADMIN);

        return UserResponse.SignUpResponse.of(user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(
            @Valid @RequestBody UserRequest.SignInRequest request
    ) {
        var user = userService.authenticate(request);
        var token = tokenProvider.generateToken(user.getEmail(), user.getRole());

        return ResponseEntity.ok(token);
    }

    @DeleteMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(
            Authentication auth
    ) {
        userService.deactivateUser(auth);

        return ResponseEntity.ok("Deactivated Done");
    }
}
