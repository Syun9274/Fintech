package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.TransactionRequest;
import com.zerobase.fintech.controller.dto.response.TransactionResponse;
import com.zerobase.fintech.entity.TransactionEntity;
import com.zerobase.fintech.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionResponse.DepositResponse deposit(
            @Valid @RequestBody TransactionRequest request,
            Authentication auth
    ) {
        TransactionEntity transaction = transactionService.deposit(auth, request);

        return TransactionResponse.DepositResponse.of(transaction);
    }

    @PostMapping("/withdrawal")
    public TransactionResponse.WithdrawalResponse withdrawal(
            @Valid @RequestBody TransactionRequest request,
            Authentication auth
    ) {
        TransactionEntity transaction = transactionService.withdrawal(auth, request);

        return TransactionResponse.WithdrawalResponse.of(transaction);
    }

    @PostMapping("/transfer")
    public TransactionResponse.TransferResponse transfer(
            @Valid @RequestBody TransactionRequest.TransferRequest request,
            Authentication auth
    ) {
        TransactionEntity transaction = transactionService.transfer(auth, request);

        return TransactionResponse.TransferResponse.of(transaction);
    }

    @GetMapping("/history/{accountNumber}")
    public Slice<TransactionResponse.HistoryResponse> getTransactionsHistory(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable,
            Authentication auth
    ) {
        Slice<TransactionEntity> transactionHistories =
                transactionService.showTransactionHistory(auth, accountNumber, pageable);

        return TransactionResponse.HistoryResponse.of(transactionHistories, accountNumber);
    }

}
