package com.zerobase.fintech.controller.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

public class AccountRequest {

    @Data
    public static class CreateRequest {
        private String accountAlias;
    }

    @Data
    public static class UpdateRequest {
        private String accountAlias;
    }

    @Data
    public static class AddRequest {

        @NotBlank(message = "Bank name is required")
        private String bankName;

        @NotBlank(message = "Account Number is required")
        @Pattern(
                regexp = "\\d{10,20}",
                message = "Account number must be between 10 and 20 digits"
        )
        private String accountNumber;

        private String accountAlias;

        @NotNull(message = "Balance is required")
        @DecimalMin(
                value = "0.0",
                message = "Balance must be non-negative"
        )
        private BigDecimal balance;
    }
}