package com.zerobase.fintech.controller.dto.request;

import lombok.Data;

public class AccountRequest {

    @Data
    public static class createAccountRequest {
        private String accountAlias;
    }

}
