package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.entity.AccountEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    @Data
    public static class CreateResponse {

        private String bankName;
        private String accountAlias;
        private String accountNumber;

        public static CreateResponse of(AccountEntity account) {
            CreateResponse response = new CreateResponse();

            response.bankName = account.getBankName();
            response.accountAlias = account.getAccountAlias();
            response.accountNumber = account.getAccountNumber();

            return response;
        }
    }

    @Data
    public static class UpdateResponse {

        private String beforeAccountNumber;
        private String afterAccountNumber;

        public static UpdateResponse of(String accountNumber,
                                        AccountEntity account) {
            UpdateResponse response = new UpdateResponse();

            response.beforeAccountNumber = accountNumber;
            response.afterAccountNumber = account.getAccountNumber();

            return response;
        }
    }

    @Data
    public static class CloseResponse {

        private String accountNumber;
        private String accountAlias;
        private AccountStatus accountStatus;
        private LocalDateTime closeTime;

        public static CloseResponse of(AccountEntity account) {
            CloseResponse response = new CloseResponse();

            response.accountNumber = account.getAccountNumber();
            response.accountAlias = account.getAccountAlias();
            response.accountStatus = account.getAccountStatus();
            response.closeTime = account.getClosedAt();

            return response;
        }
    }

    @Data
    public static class AddResponse {

        private String bankName;
        private String accountAlias;
        private String accountNumber;
        private BigDecimal balance;

        public static AddResponse of(AccountEntity account) {
            AddResponse response = new AddResponse();

            response.bankName = account.getBankName();
            response.accountAlias = account.getAccountAlias();
            response.accountNumber = account.getAccountNumber();
            response.balance = account.getBalance();

            return response;
        }
    }

    @Data
    public static class DeleteResponse {

        private String bankName;
        private String accountAlias;
        private String accountNumber;
        private AccountStatus accountStatus;

        public static DeleteResponse of(AccountEntity account) {
            DeleteResponse response = new DeleteResponse();

            response.bankName = account.getBankName();
            response.accountAlias = account.getAccountAlias();
            response.accountNumber = account.getAccountNumber();
            response.accountStatus = account.getAccountStatus();

            return response;
        }
    }
}
