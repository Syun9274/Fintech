package com.zerobase.fintech.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@Embeddable
public class AccountSnapshot {

    private BigDecimal senderBalanceBefore;
    private BigDecimal senderBalanceAfter;

    private BigDecimal receiverBalanceBefore;
    private BigDecimal receiverBalanceAfter;

    public AccountSnapshot() { }
}
