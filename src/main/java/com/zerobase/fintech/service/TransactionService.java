package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.common.util.TransactionValidator;
import com.zerobase.fintech.controller.dto.request.TransactionRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.AccountSnapshot;
import com.zerobase.fintech.entity.TransactionEntity;
import com.zerobase.fintech.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountValidator accountValidator;
    private final TransactionValidator transactionValidator;

    public TransactionEntity deposit(TransactionRequest request) {

        // 거래 예정 금액
        BigDecimal amount = request.getAmount();

        TransactionEntity transaction = initializeTransaction(
                TransactionType.DEPOSIT,
                "입금");

        try {
            // TODO: 인증 기능 구현 후 계좌 소유주 검증 단계 추가
            // 거래와 관련된 모든 정보 검증
            accountValidator.validateAccountNumber(request.getAccountNumber());
            AccountEntity account = accountValidator.validateAccountExists(request.getAccountNumber());
            accountValidator.validateAccountNotActive(account);
            transactionValidator.validateTransactionAmountIsPositive(amount);

            // 검증 후 거래 기록 저장
            transaction.setReceiverAccount(account);
            transaction.setAmount(amount);

            // snapshot 생성 및 저장
            AccountSnapshot snapshot = createAccountSnapshot(null, account, amount);
            transaction.setSnapshot(snapshot);

            // 입금 작업 실시
            performDeposit(account, amount);

        } catch (Exception e) {
            // 거래 실패 처리
            return saveTransaction(transaction, TransactionStatus.FAILED);
        }

        // 거래 완료
        return saveTransaction(transaction, TransactionStatus.SUCCESS);
    }

    public TransactionEntity withdrawal(TransactionRequest request) {

        // 거래 예정 금액
        BigDecimal amount = request.getAmount();

        TransactionEntity transaction = initializeTransaction(
                TransactionType.WITHDRAWAL,
                "출금");

        try {
            // TODO: 인증 기능 구현 후 계좌 소유주 검증 단계 추가
            // 거래와 관련된 모든 정보 검증
            accountValidator.validateAccountNumber(request.getAccountNumber());
            AccountEntity account = accountValidator.validateAccountExists(request.getAccountNumber());
            accountValidator.validateAccountNotActive(account);
            accountValidator.validateBalanceIsMoreThanAmount(account, amount);
            transactionValidator.validateTransactionAmountIsPositive(amount);

            // 검증 후 거래 기록 저장
            transaction.setSenderAccount(account);
            transaction.setAmount(amount);

            // snapshot 생성 및 저장
            AccountSnapshot snapshot = createAccountSnapshot(account, null, amount);
            transaction.setSnapshot(snapshot);

            // 출금 작업 실시
            performWithdrawal(account, amount);

        } catch (Exception e) {
            // 거래 실패 처리
            return saveTransaction(transaction, TransactionStatus.FAILED);
        }

        // 거래 완료
        return saveTransaction(transaction, TransactionStatus.SUCCESS);
    }

    // 거래 기록 생성
    private TransactionEntity initializeTransaction(TransactionType type, String memo) {
        return TransactionEntity.builder()
                .transactionType(type)
                .memo(memo)
                .transactionCreatedAt(LocalDateTime.now())
                .build();
    }

    // 스냅샷 생성
    private AccountSnapshot createAccountSnapshot(AccountEntity senderAccount,
                                                  AccountEntity receiverAccount,
                                                  BigDecimal amount) {
        return AccountSnapshot.builder()
                .senderBalanceBefore(senderAccount != null ? senderAccount.getBalance() : BigDecimal.ZERO)
                .receiverBalanceBefore(receiverAccount != null ? receiverAccount.getBalance() : BigDecimal.ZERO)
                .senderBalanceAfter(senderAccount != null ? senderAccount.getBalance().subtract(amount) : null)
                .receiverBalanceAfter(receiverAccount != null ? receiverAccount.getBalance().add(amount) : null)
                .build();
    }

    // 입금
    private void performDeposit(AccountEntity account, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }

    // 출금
    private void performWithdrawal(AccountEntity account, BigDecimal amount) {
        account.setBalance(account.getBalance().subtract(amount));
    }

    // 거래 상태 저장
    private TransactionEntity saveTransaction(TransactionEntity transaction, TransactionStatus transactionStatus) {
        transaction.setTransactionStatus(transactionStatus);
        transaction.setTransactionClosedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }
}
