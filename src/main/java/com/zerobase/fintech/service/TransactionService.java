package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.common.enums.TransactionType;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.common.util.TransactionValidator;
import com.zerobase.fintech.common.util.UserValidator;
import com.zerobase.fintech.controller.dto.request.TransactionRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.AccountSnapshot;
import com.zerobase.fintech.entity.TransactionEntity;
import com.zerobase.fintech.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountValidator accountValidator;
    private final TransactionValidator transactionValidator;
    private final UserValidator userValidator;

    @Transactional
    public TransactionEntity deposit(Authentication auth, TransactionRequest request) {

        // 거래 예정 금액
        BigDecimal amount = request.getAmount();

        TransactionEntity transaction = initializeTransaction(
                TransactionType.DEPOSIT,
                "입금");

        try {
            // 계좌 검증
            AccountEntity account = validateDepositAccount(request.getAccountNumber());

            // 계좌 소유주 검증
            validateAccountAndUserIdMatch(auth, account);

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

    @Transactional
    public TransactionEntity withdrawal(Authentication auth, TransactionRequest request) {

        // 거래 예정 금액
        BigDecimal amount = request.getAmount();

        TransactionEntity transaction = initializeTransaction(
                TransactionType.WITHDRAWAL,
                "출금");

        try {
            // 계좌 검증
            AccountEntity account = validateWithdrawalAccount(request.getAccountNumber(), amount);

            // 계좌 소유주 검증
            validateAccountAndUserIdMatch(auth, account);

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

    @Transactional
    public TransactionEntity transfer(Authentication auth, TransactionRequest.TransferRequest request) {

        // sender, receiver 불일치 검증
        transactionValidator.validateTransactionAccountsAreDifferent(
                request.getAccountNumber(),
                request.getReceiverAccountNumber());

        // 거래 예정 금액
        BigDecimal amount = request.getAmount();

        // memo 설정
        TransactionEntity transaction = initializeTransaction(
                TransactionType.TRANSFER,
                request.getMemo());

        try {
            // TODO: 인증 기능 구현 후 계좌 소유주 검증 단계 추가
            // 출금 계좌 검증
            AccountEntity senderAccount = validateWithdrawalAccount(request.getAccountNumber(), amount);

            // 입금 계좌 검증
            AccountEntity receiverAccount = validateDepositAccount(request.getReceiverAccountNumber());

            // 계좌 소유주 검증
            validateAccountAndUserIdMatch(auth, senderAccount);

            // 검증 후 거래 기록 저장
            transaction.setSenderAccount(senderAccount);
            transaction.setReceiverAccount(receiverAccount);
            transaction.setAmount(amount);

            // snapshot 생성 및 저장
            AccountSnapshot snapshot = createAccountSnapshot(senderAccount, receiverAccount, amount);
            transaction.setSnapshot(snapshot);

            // 송금 작업 실시
            performWithdrawal(senderAccount, amount);
            performDeposit(receiverAccount, amount);

        } catch (Exception e) {
            // 거래 실패 처리
            return saveTransaction(transaction, TransactionStatus.FAILED);
        }

        // 거래 완료
        return saveTransaction(transaction, TransactionStatus.SUCCESS);
    }

    // 거래 기록 불러오기
    public Slice<TransactionEntity> showTransactionHistory(Authentication auth, String accountNumber, Pageable pageable) {

        // 계좌 검증
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);

        // 계좌 소유주 검증
        validateAccountAndUserIdMatch(auth, account);

        return transactionRepository.findByTransactionStatusAndSenderAccountOrReceiverAccount(
                TransactionStatus.SUCCESS,
                account,
                account,
                pageable
        );
    }

    // 출금 계좌 검증
    private AccountEntity validateWithdrawalAccount(String accountNumber, BigDecimal amount) {
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateAccountNotActive(account);
        accountValidator.validateBalanceIsMoreThanAmount(account, amount);
        transactionValidator.validateTransactionAmountIsPositive(amount);

        return account;
    }

    // 입금 계좌 검증
    private AccountEntity validateDepositAccount(String accountNumber) {
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateAccountNotActive(account);

        return account;
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

    // 계좌 소유주 검증
    private void validateAccountAndUserIdMatch(Authentication auth, AccountEntity account) {
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);
        accountValidator.validateAccountUserId(account, userId);
        userValidator.userStatusIsActive(userId);
    }
}