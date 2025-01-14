package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.controller.dto.response.AccountResponse;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse.CreateResponse createAccount(
            @RequestBody AccountRequest.CreateRequest request) {
        AccountEntity account = accountService.createAccount(request);

        return AccountResponse.CreateResponse.of(account);
    }

    @PutMapping("/{accountNumber}")
    public AccountResponse.UpdateResponse updateAccount(
            @PathVariable String accountNumber,
            @RequestBody AccountRequest.UpdateRequest request) {
        AccountEntity account = accountService.updateAccount(accountNumber, request);

        return AccountResponse.UpdateResponse.of(accountNumber, account);
    }

    @DeleteMapping("/{accountNumber}")
    public AccountResponse.CloseResponse closeAccount(
            @PathVariable String accountNumber) {
        AccountEntity account = accountService.closeAccount(accountNumber);

        return AccountResponse.CloseResponse.of(account);
    }

    @PostMapping("/external")
    public AccountResponse.AddExternalResponse addExternalAccount(
            @Valid @RequestBody AccountRequest.AddRequest request) {
        AccountEntity account = accountService.addExternalAccount(request);

        return AccountResponse.AddExternalResponse.of(account);
    }

    @DeleteMapping("/external/{accountNumber}")
    public AccountResponse.DeleteExternalResponse deleteExternalAccount(
            @PathVariable String accountNumber) {
        AccountEntity account = accountService.deleteExternalAccount(accountNumber);

        return AccountResponse.DeleteExternalResponse.of(account);
    }

    @GetMapping("/list/{userId}")
    public List<AccountResponse.ListResponse> getAccountListByUserId(
            @PathVariable Long userId) {
        List<AccountEntity> accountList = accountService.showAccountList(userId);

        return AccountResponse.ListResponse.of(accountList);
    }
}
