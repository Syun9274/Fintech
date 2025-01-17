package com.zerobase.fintech.common.util;

import com.zerobase.fintech.common.enums.UserStatus;
import com.zerobase.fintech.entity.UserEntity;
import com.zerobase.fintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {

    private final UserRepository userRepository;

    // 이메일 또는 휴대폰 번호 가입 가능 여부 확인
    public void AlreadyExist(String email, String phoneNumber) {
        boolean emailExists = userRepository.existsByEmail(email);
        boolean phoneExists = userRepository.existsByPhoneNumber(phoneNumber);

        if (emailExists || phoneExists) {
            throw new IllegalArgumentException(
                    String.format("Email or phone number already exists. Email: %s, Phone: %s", email, phoneNumber)
            );
        }
    }

    // 사용자 인증 후 userEntity 반환
    public UserEntity findUserByAuth(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }
        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // 사용자 인증 후 userid 반환
    public Long findUserByAuthAndGetUserId(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName();

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return user.getId();
    }

    // 계정 상태 검증
    public void userStatusIsActive(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getStatus() == UserStatus.DEACTIVATED) {
            throw new IllegalArgumentException("User is not active");
        }
    }
}
