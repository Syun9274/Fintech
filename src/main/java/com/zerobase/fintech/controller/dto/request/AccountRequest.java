package com.zerobase.fintech.controller.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

public class AccountRequest {

    @Data
    public static class CreateRequest {
        private String accountAlias;
    }

    @Data
    public static class UpdateRequest extends BaseAccountRequest { }

    @Data
    public static class CloseRequest extends BaseAccountRequest { }

    @Data
    public static class AddRequest{

        @NotBlank(message = "Bank name is required")
        private String bankName;

        @Valid
        private BaseAccountRequest accountNumberRequest;

        private String accountAlias;

        @NotNull(message = "Balance is required")
        @DecimalMin(
                value = "0.0",
                message = "Balance must be non-negative"
        )
        private BigDecimal balance;
    }

    @Data
    public static class DeleteRequest extends BaseAccountRequest { }
}