package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.entity.TransactionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionResponse {

    @Data
    public static class BaseResponse {

        private String amount;
        private String balance;
        private String memo;
        private TransactionType transactionType;
        private TransactionStatus transactionStatus;

        public void populateCommonFields(TransactionEntity transaction) {
            this.amount = transaction.getAmount().toString();
            this.memo = transaction.getMemo();
            this.transactionType = transaction.getTransactionType();
            this.transactionStatus = transaction.getTransactionStatus();
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class DepositResponse extends BaseResponse {
        private String receiverAccountNumber;

        public static DepositResponse of(TransactionEntity transaction) {
            DepositResponse response = new DepositResponse();

            response.populateCommonFields(transaction);
            response.receiverAccountNumber = transaction.getReceiverAccount().getAccountNumber();
            response.setBalance(transaction.getReceiverAccount().getBalance().toString());

            return response;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class WithdrawalResponse extends BaseResponse {
        private String senderAccountNumber;

        public static WithdrawalResponse of(TransactionEntity transaction) {
            WithdrawalResponse response = new WithdrawalResponse();

            response.populateCommonFields(transaction);
            response.senderAccountNumber = transaction.getSenderAccount().getAccountNumber();
            response.setBalance(transaction.getSenderAccount().getBalance().toString());

            return response;
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class TransferResponse extends BaseResponse {
        private String senderAccountNumber;
        private String receiverAccountNumber;

        public static TransferResponse of(TransactionEntity transaction) {
            TransferResponse response = new TransferResponse();

            response.populateCommonFields(transaction);
            response.senderAccountNumber = transaction.getSenderAccount().getAccountNumber();
            response.receiverAccountNumber = transaction.getReceiverAccount().getAccountNumber();
            response.setBalance(transaction.getSenderAccount().getBalance().toString());

            return response;
        }
    }

    @Data
    public static class HistoryResponse {

        private String amount;
        private String balance;
        private String memo;

        public static List<HistoryResponse> of(List<TransactionEntity> transactions, String targetAccountNumber) {
            List<HistoryResponse> responses = new ArrayList<>();

            for (TransactionEntity transaction : transactions) {
                HistoryResponse response = new HistoryResponse();

                response.amount = transaction.getAmount().toString();

                // 요청된 계좌의 잔액만 선택
                if (transaction.getSenderAccount() != null &&
                        transaction.getSenderAccount().getAccountNumber().equals(targetAccountNumber)) {
                    response.balance = transaction.getSnapshot().getSenderBalanceAfter().toString();
                } else if (transaction.getReceiverAccount() != null &&
                        transaction.getReceiverAccount().getAccountNumber().equals(targetAccountNumber)) {
                    response.balance = transaction.getSnapshot().getReceiverBalanceAfter().toString();
                }

                response.memo = transaction.getMemo();

                responses.add(response);
            }

            return responses;
        }
    }
}