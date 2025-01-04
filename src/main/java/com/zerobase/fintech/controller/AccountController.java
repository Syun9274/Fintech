package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.controller.dto.response.AccountResponse;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("api/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public AccountResponse.CreateResponse createAccount(
            @RequestBody AccountRequest.CreateRequest request) {
        AccountEntity account = accountService.createAccount(request);

        return AccountResponse.CreateResponse.of(account);
    }

    @PutMapping("update")
    public AccountResponse.UpdateResponse updateAccount(
            @Valid @RequestBody AccountRequest.UpdateRequest request) {
        AccountEntity account = accountService.updateAccount(request);

        return AccountResponse.UpdateResponse.of(request.getAccountNumber(), account);
    }

    @DeleteMapping("close")
    public AccountResponse.CloseResponse closeAccount(
            @Valid @RequestBody AccountRequest.CloseRequest request) {
        AccountEntity account = accountService.closeAccount(request);

        return AccountResponse.CloseResponse.of(account);
    }

    @PostMapping("add")
    public AccountResponse.AddResponse addAccount(
            @Valid @RequestBody AccountRequest.AddRequest request) {
        AccountEntity account = accountService.addAccount(request);

        return AccountResponse.AddResponse.of(account);
    }

    @DeleteMapping("delete")
    public AccountResponse.DeleteResponse deleteAccount(
            @Valid @RequestBody AccountRequest.DeleteRequest request) {
        AccountEntity account = accountService.deleteAccount(request);

        return AccountResponse.DeleteResponse.of(account);
    }
}
