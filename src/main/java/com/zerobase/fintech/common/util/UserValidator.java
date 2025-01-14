package com.zerobase.fintech.common.util;

import com.zerobase.fintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
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
}
