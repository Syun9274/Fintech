package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.TransactionRequest;
import com.zerobase.fintech.controller.dto.response.TransactionResponse;
import com.zerobase.fintech.entity.TransactionEntity;
import com.zerobase.fintech.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/transactions")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionResponse.DepositResponse deposit(
            @Valid @RequestBody TransactionRequest request
    ) {
        TransactionEntity transaction = transactionService.deposit(request);

        return TransactionResponse.DepositResponse.of(transaction);
    }

    @PostMapping("/withdrawal")
    public TransactionResponse.WithdrawalResponse withdrawal(
            @Valid @RequestBody TransactionRequest request
    ) {
        TransactionEntity transaction = transactionService.withdrawal(request);

        return TransactionResponse.WithdrawalResponse.of(transaction);
    }

    @PostMapping("/transfer")
    public TransactionResponse.TransferResponse transfer(
            @Valid @RequestBody TransactionRequest.TransferRequest request
    ) {
        TransactionEntity transaction = transactionService.transfer(request);

        return TransactionResponse.TransferResponse.of(transaction);
    }

    @GetMapping("history/{accountNumber}")
    public List<TransactionResponse.HistoryResponse> getTransactionsHistory(
            @PathVariable String accountNumber
    ) {
        List<TransactionEntity> transactionHistories = transactionService.showTransactionHistory(accountNumber);

        return TransactionResponse.HistoryResponse.of(transactionHistories, accountNumber);
    }

}
