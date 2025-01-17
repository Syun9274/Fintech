package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.entity.UserEntity;

public class UserResponse {

    public record SignUpResponse(
            String email,
            String username
    ) {
        public static SignUpResponse of(UserEntity user) {
            return new SignUpResponse(
                    user.getEmail(),
                    user.getUsername()
            );
        }
    }
}
