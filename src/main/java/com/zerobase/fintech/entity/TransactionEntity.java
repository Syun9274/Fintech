package com.zerobase.fintech.entity;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
@Entity
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity senderAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity receiverAccount;

    private String memo;

    private BigDecimal amount;

    @Embedded
    private AccountSnapshot snapshot;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionCreatedAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime transactionClosedAt;
}
