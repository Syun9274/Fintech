package com.zerobase.fintech.controller.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

    // 이메일 형식
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    // 비밀번호 형식
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @NotBlank(message = "Password is required")
    private String password;

    @Data
    public static class SignUpRequest extends UserRequest {

        // 사용자 이름
        @Size(max = 20, message = "User name must not exceed 20 characters")
        private String nickname;

        // 휴대폰 번호
        @NotBlank(message = "phoneNumber is required")
        @Pattern(
                regexp = "^(01[0-9])-?([0-9]{3,4})-?([0-9]{4})$",
                message = "Invalid phone number format. Expected: 010-1234-5678 or 01012345678"
        )
        private String phoneNumber;
    }

    @Data
    public static class SignInRequest extends UserRequest { }
}
