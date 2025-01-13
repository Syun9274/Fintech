package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.entity.AccountEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AccountResponse {

    public record CreateResponse(
            String bankName,
            String accountAlias,
            String accountNumber
    ) {
        public static CreateResponse of(AccountEntity account) {
            return new CreateResponse(
                    account.getBankName(),
                    account.getAccountAlias(),
                    account.getAccountNumber()
            );
        }
    }

    public record UpdateResponse(
            String beforeAccountNumber,
            String afterAccountNumber) {
        public static UpdateResponse of(String accountNumber, AccountEntity account) {
            return new UpdateResponse(
                    accountNumber,
                    account.getAccountNumber()
            );
        }
    }

    public record CloseResponse(
            String accountNumber,
            String accountAlias,
            AccountStatus accountStatus,
            LocalDateTime closeTime) {
        public static CloseResponse of(AccountEntity account) {
            return new CloseResponse(
                    account.getAccountNumber(),
                    account.getAccountAlias(),
                    account.getAccountStatus(),
                    account.getClosedAt()
            );
        }
    }

    public record AddExternalResponse(
            String bankName,
            String accountAlias,
            String accountNumber,
            BigDecimal balance) {
        public static AddExternalResponse of(AccountEntity account) {
            return new AddExternalResponse(
                    account.getBankName(),
                    account.getAccountAlias(),
                    account.getAccountNumber(),
                    account.getBalance()
            );
        }
    }

    public record DeleteExternalResponse(
            String bankName,
            String accountAlias,
            String accountNumber,
            AccountStatus accountStatus) {
        public static DeleteExternalResponse of(AccountEntity account) {
            return new DeleteExternalResponse(
                    account.getBankName(),
                    account.getAccountAlias(),
                    account.getAccountNumber(),
                    account.getAccountStatus()
            );
        }
    }

    public record ListResponse(
            String accountNumber,
            String accountAlias,
            BigDecimal balance,
            AccountStatus accountStatus) {
        public static List<ListResponse> of(List<AccountEntity> accounts) {
            List<ListResponse> responses = new ArrayList<>();

            for (AccountEntity accountEntity : accounts) {
                responses.add(new ListResponse(
                        accountEntity.getAccountNumber(),
                        accountEntity.getAccountAlias(),
                        accountEntity.getBalance(),
                        accountEntity.getAccountStatus()
                ));
            }

            return responses;
        }
    }
}

