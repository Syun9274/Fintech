package com.zerobase.fintech.common.util;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class AccountValidator {

    private final AccountRepository accountRepository;

    // 계좌 번호 형식 확인
    public void validateAccountNumber(String accountNumber) {
        if (!accountNumber.matches("\\d{10,20}")) {
            throw new IllegalArgumentException("Account number must be between 10 and 20 digits");
        }
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
    public void validateAccountIsClosed(AccountEntity account) {
        if (Objects.equals(account.getAccountStatus(), AccountStatus.CLOSED)) {
            throw new IllegalArgumentException("Account is already closed");
        }
    }

    // 계좌 활성화 여부 확인
    public void validateAccountNotActive(AccountEntity account) {
        if (!Objects.equals(account.getAccountStatus(), AccountStatus.ACTIVE)) {
            throw new IllegalArgumentException("Account is not active");
        }
    }

     // 계좌 잔액 확인
    public void validateBalanceIsZero(AccountEntity account) {
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new IllegalArgumentException("Account balance must be zero before closing the account");
        }
    }

    // 계좌 출금 잔액 확인
    public void validateBalanceIsMoreThanAmount(AccountEntity account, BigDecimal amount) {
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Account balance must be equal to or greater than the amount");
        }
    }
}
