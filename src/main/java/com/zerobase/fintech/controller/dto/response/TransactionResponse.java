package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.entity.TransactionEntity;
import lombok.Data;

@Data
public class TransactionResponse {

    @Data
    public static class DepositResponse {

        private String receiverAccountNumber;
        private String amount;
        private String balance;
        private String memo;
        private TransactionType transactionType;
        private TransactionStatus transactionStatus;

        public static DepositResponse of(TransactionEntity transaction) {
            DepositResponse response = new DepositResponse();

            response.receiverAccountNumber = transaction.getReceiverAccount().getAccountNumber();
            response.amount = transaction.getAmount().toString();
            response.balance = transaction.getReceiverAccount().getBalance().toString();
            response.memo = transaction.getMemo();
            response.transactionType = transaction.getTransactionType();
            response.transactionStatus = transaction.getTransactionStatus();

            return response;
        }
    }

    @Data
    public static class WithdrawalResponse {

        private String senderAccountNumber;
        private String amount;
        private String balance;
        private String memo;
        private TransactionType transactionType;
        private TransactionStatus transactionStatus;

        public static WithdrawalResponse of(TransactionEntity transaction) {
            WithdrawalResponse response = new WithdrawalResponse();

            response.senderAccountNumber = transaction.getSenderAccount().getAccountNumber();
            response.amount = transaction.getAmount().toString();
            response.balance = transaction.getSenderAccount().getBalance().toString();
            response.memo = transaction.getMemo();
            response.transactionType = transaction.getTransactionType();
            response.transactionStatus = transaction.getTransactionStatus();

            return response;
        }
    }

    @Data
    public static class TransferResponse {

        private String senderAccountNumber;
        private String receiverAccountNumber;
        private String amount;
        private String balance;
        private String memo;
        private TransactionType transactionType;
        private TransactionStatus transactionStatus;

        public static TransferResponse of(TransactionEntity transaction) {
            TransferResponse response = new TransferResponse();

            response.senderAccountNumber = transaction.getSenderAccount().getAccountNumber();
            response.receiverAccountNumber = transaction.getReceiverAccount().getAccountNumber();
            response.amount = transaction.getAmount().toString();
            response.balance = transaction.getSenderAccount().getBalance().toString();
            response.memo = transaction.getMemo();
            response.transactionType = transaction.getTransactionType();
            response.transactionStatus = transaction.getTransactionStatus();

            return response;
        }
    }
}
