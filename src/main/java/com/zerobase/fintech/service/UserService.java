package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.UserRole;
import com.zerobase.fintech.common.enums.UserStatus;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.common.util.UserValidator;
import com.zerobase.fintech.controller.dto.request.UserRequest;
import com.zerobase.fintech.entity.UserEntity;
import com.zerobase.fintech.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountValidator accountValidator;
    private final UserValidator userValidator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(IllegalAccessError::new);
    }

    @Transactional
    public UserEntity register(UserRequest.SignUpRequest request) {

        String email = request.getEmail();
        String phoneNumber = request.getPhoneNumber();
        String nickname = request.getNickname();

        // 이메일 또는 휴대폰 번호 가입 여부 확인
        userValidator.AlreadyExist(email, phoneNumber);

        return userRepository.save(UserEntity.builder()
                .email(email)
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .status(UserStatus.ACTIVE)
                .role(UserRole.ROLE_USER)
                .createdAt(LocalDateTime.now())
                .build());
    }

    public UserEntity authenticate(UserRequest.SignInRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(IllegalAccessError::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        return user;
    }

    @Transactional
    public void deactivateUser(Authentication auth) {
        UserEntity user = userValidator.findUserByAuth(auth);

        accountValidator.validateAccountIsAllClosed(user.getId());

        user.setStatus(UserStatus.DEACTIVATED);
    }
}
