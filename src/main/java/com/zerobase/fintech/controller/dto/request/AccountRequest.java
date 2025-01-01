package com.zerobase.fintech.controller.dto.request;

import lombok.Data;

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

}
