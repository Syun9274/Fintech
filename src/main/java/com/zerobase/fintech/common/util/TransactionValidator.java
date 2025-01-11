package com.zerobase.fintech.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

@Component
public class TransactionValidator {

    // 거래 금액 양수 확인
    public void validateTransactionAmountIsPositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero and not null");
        }
    }

    // 거래 대상 계좌 불일치 검증
    public void validateTransactionAccountsAreDifferent(String account1, String account2) {
        // 입력 값 검증
        if (account1 == null || account2 == null) {
            throw new IllegalArgumentException("Account numbers cannot be null");
        }

        // 계좌 번호 일치 여부 확인
        if (Objects.equals(account1, account2)) {
            throw new IllegalArgumentException("Transaction cannot occur between the same accounts");
        }
    }
}
