package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.entity.AccountEntity;
import lombok.Data;

public class AccountResponse {

    @Data
    public static class createAccountResponse {

        private String bankName;
        private String accountAlias;
        private String accountNumber;

        public static createAccountResponse of(AccountEntity account) {
            createAccountResponse response = new createAccountResponse();

            response.bankName = account.getBankName();
            response.accountAlias = account.getAccountAlias();
            response.accountNumber = account.getAccountNumber();

            return response;
        }
    }

    @Data
    public static class updateAccountResponse {

        private String beforeAccountNumber;
        private String afterAccountNumber;

        public static updateAccountResponse of(String accountNumber,
                                               AccountEntity account) {
            updateAccountResponse response = new updateAccountResponse();

            response.beforeAccountNumber = accountNumber;
            response.afterAccountNumber = account.getAccountNumber();

            return response;
        }
    }
}
