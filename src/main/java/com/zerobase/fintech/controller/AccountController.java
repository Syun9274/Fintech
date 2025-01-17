package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.controller.dto.response.AccountResponse;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('USER')")
@RequiredArgsConstructor
@RequestMapping("/api/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public AccountResponse.CreateResponse createAccount(
            @RequestBody AccountRequest.CreateRequest request,
            Authentication auth
    ) {
        AccountEntity account = accountService.createAccount(auth, request);

        return AccountResponse.CreateResponse.of(account);
    }

    @PutMapping("/{accountNumber}")
    public AccountResponse.UpdateResponse updateAccount(
            @PathVariable String accountNumber,
            @RequestBody AccountRequest.UpdateRequest request,
            Authentication auth
    ) {
        AccountEntity account = accountService.updateAccount(auth, accountNumber, request);

        return AccountResponse.UpdateResponse.of(accountNumber, account);
    }

    @DeleteMapping("/{accountNumber}")
    public AccountResponse.CloseResponse closeAccount(
            @PathVariable String accountNumber,
            Authentication auth
    ) {
        AccountEntity account = accountService.closeAccount(auth, accountNumber);

        return AccountResponse.CloseResponse.of(account);
    }

    @PostMapping("/external")
    public AccountResponse.AddExternalResponse addExternalAccount(
            @Valid @RequestBody AccountRequest.AddRequest request,
            Authentication auth
    ) {
        AccountEntity account = accountService.addExternalAccount(auth, request);

        return AccountResponse.AddExternalResponse.of(account);
    }

    @DeleteMapping("/external/{accountNumber}")
    public AccountResponse.DeleteExternalResponse deleteExternalAccount(
            @PathVariable String accountNumber,
            Authentication auth
    ) {
        AccountEntity account = accountService.deleteExternalAccount(auth, accountNumber);

        return AccountResponse.DeleteExternalResponse.of(account);
    }

    @GetMapping("/list/{userId}")
    public List<AccountResponse.ListResponse> getAccountListByUserId(
            @PathVariable Long userId,
            Authentication auth
    ) {
        List<AccountEntity> accountList = accountService.showAccountList(auth, userId);

        return AccountResponse.ListResponse.of(accountList);
    }
}
