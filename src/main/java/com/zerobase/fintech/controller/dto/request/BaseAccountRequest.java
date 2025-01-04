package com.zerobase.fintech.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

// 공통 코드 통합
@Data
public class BaseAccountRequest {

    @NotBlank(message = "Account Number is required")
    @Pattern(
            regexp = "\\d{10,20}",
            message = "Account number must be between 10 and 15 digits"
    )
    private String accountNumber;
}
