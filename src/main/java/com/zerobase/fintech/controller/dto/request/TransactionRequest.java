package com.zerobase.fintech.controller.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequest {

    // 계좌 번호 형식
    @NotBlank(message = "Account Number is required")
    @Pattern(
            regexp = "\\d{10,20}",
            message = "Account number must be between 10 and 20 digits"
    )
    private String accountNumber;

    // 금액 형식
    @NotNull(message = "Amount is required")
    @DecimalMin(
            value = "0.01", message = "Balance must be non-negative"
    )
    private BigDecimal amount;

    @Data
    public static class TransferRequest extends TransactionRequest {

        @NotBlank(message = "Account Number is required")
        @Pattern(
                regexp = "\\d{10,20}",
                message = "Account number must be between 10 and 20 digits"
        )
        private String receiverAccountNumber;

        private String memo;
    }
}