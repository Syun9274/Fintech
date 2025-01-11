package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.entity.TransactionEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
}