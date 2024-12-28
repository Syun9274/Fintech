package com.zerobase.fintech.controller;

import com.zerobase.fintech.controller.dto.request.AccountRequest.*;
import com.zerobase.fintech.controller.dto.response.AccountResponse.*;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/accounts")
@RestController
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public createAccountResponse createAccount(@RequestBody createAccountRequest request) {
        AccountEntity account = accountService.createAccount(request);

        return createAccountResponse.of(account);
    }

    @PostMapping("update")
    public updateAccountResponse updateAccount(@RequestBody updateAccountRequest request) {
        AccountEntity account = accountService.updateAccount(request);

        return updateAccountResponse.of(request.getAccountNumber(), account);
    }

}
