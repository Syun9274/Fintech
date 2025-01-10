package com.zerobase.fintech.common.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class TransactionValidator {

    // 거래 금액 양수 확인
    public void validateTransactionAmountIsPositive(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transaction amount must be greater than zero and not null");
        }
    }
}
