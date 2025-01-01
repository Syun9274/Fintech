package com.zerobase.fintech.common.util;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.repository.AccountRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class AccountValidator {

    private final AccountRepository accountRepository;

    public AccountValidator(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // 계좌 존재 여부 확인
    public AccountEntity validateAccountExists(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    }

    // 은행 명 확인
    public void validateBankName(AccountEntity account) {
        if (!Objects.equals(account.getBankName(), "ZB_Bank")) {
            throw new IllegalArgumentException("Account is not ZB_Bank");
        }
    }

    // 계좌 해지 여부 확인
    public void validateAccountNotClosed(AccountEntity account) {
        if (Objects.equals(account.getAccountStatus(), AccountStatus.CLOSED)) {
            throw new IllegalArgumentException("Account is already closed");
        }
    }

     // 계좌 잔액 확인
    public void validateBalanceIsZero(AccountEntity account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Account balance must be zero before closing the account");
        }
    }
}
