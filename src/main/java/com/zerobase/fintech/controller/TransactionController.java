package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.TransactionRequest;
import com.zerobase.fintech.controller.dto.response.TransactionResponse;
import com.zerobase.fintech.entity.TransactionEntity;
import com.zerobase.fintech.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/transactions")
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public TransactionResponse.DepositResponse deposit(
            @Valid @RequestBody TransactionRequest.DepositRequest request
    ) {
        TransactionEntity transaction = transactionService.deposit(request);

        return TransactionResponse.DepositResponse.of(transaction);
    }

}
