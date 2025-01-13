package com.zerobase.fintech.controller.dto.response;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.entity.TransactionEntity;

import java.util.List;

public class TransactionResponse {

    public record DepositResponse(
            String receiverAccountNumber,
            String amount,
            String balance,
            String memo,
            TransactionType transactionType,
            TransactionStatus transactionStatus
    ) {
        public static DepositResponse of(TransactionEntity transaction) {
            return new DepositResponse(
                    transaction.getReceiverAccount().getAccountNumber(),
                    transaction.getAmount().toString(),
                    transaction.getReceiverAccount().getBalance().toString(),
                    transaction.getMemo(),
                    transaction.getTransactionType(),
                    transaction.getTransactionStatus()
            );
        }
    }

    public record WithdrawalResponse(
            String senderAccountNumber,
            String amount,
            String balance,
            String memo,
            TransactionType transactionType,
            TransactionStatus transactionStatus
    ) {
        public static WithdrawalResponse of(TransactionEntity transaction) {
            return new WithdrawalResponse(
                    transaction.getSenderAccount().getAccountNumber(),
                    transaction.getAmount().toString(),
                    transaction.getSenderAccount().getBalance().toString(),
                    transaction.getMemo(),
                    transaction.getTransactionType(),
                    transaction.getTransactionStatus()
            );
        }
    }

    public record TransferResponse(
            String senderAccountNumber,
            String receiverAccountNumber,
            String amount,
            String balance,
            String memo,
            TransactionType transactionType,
            TransactionStatus transactionStatus
    ) {
        public static TransferResponse of(TransactionEntity transaction) {
            return new TransferResponse(
                    transaction.getSenderAccount().getAccountNumber(),
                    transaction.getReceiverAccount().getAccountNumber(),
                    transaction.getAmount().toString(),
                    transaction.getSenderAccount().getBalance().toString(),
                    transaction.getMemo(),
                    transaction.getTransactionType(),
                    transaction.getTransactionStatus()
            );
        }
    }

    public record HistoryResponse(
            String amount,
            String balance,
            String memo
    ) {
        public static List<HistoryResponse> of(List<TransactionEntity> transactions, String targetAccountNumber) {
            return transactions.stream()
                    .map(transaction -> {
                        String balance = null;
                        if (transaction.getSenderAccount() != null &&
                                transaction.getSenderAccount().getAccountNumber().equals(targetAccountNumber)) {
                            balance = transaction.getSnapshot().getSenderBalanceAfter().toString();
                        } else if (transaction.getReceiverAccount() != null &&
                                transaction.getReceiverAccount().getAccountNumber().equals(targetAccountNumber)) {
                            balance = transaction.getSnapshot().getReceiverBalanceAfter().toString();
                        }
                        return new HistoryResponse(
                                transaction.getAmount().toString(),
                                balance,
                                transaction.getMemo()
                        );
                    })
                    .toList();
        }
    }
}
