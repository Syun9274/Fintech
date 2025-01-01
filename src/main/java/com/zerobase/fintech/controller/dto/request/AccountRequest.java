package com.zerobase.fintech.controller.dto.request;

import lombok.Data;

import java.math.BigDecimal;

public class AccountRequest {

    @Data
    public static class CreateRequest {
        private String accountAlias;
    }

    @Data
    public static class UpdateRequest {
        private String accountNumber;
    }

    @Data
    public static class CloseRequest {
        private String accountNumber;
    }

    @Data
    public static class AddRequest {
        private String bankName;
        private String accountNumber;
        private String accountAlias;
        private BigDecimal balance;
    }

}
