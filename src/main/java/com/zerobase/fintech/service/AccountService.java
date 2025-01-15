package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.common.util.AccountNumberGenerator;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.common.util.UserValidator;
import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountValidator accountValidator;
    private final UserValidator userValidator;

    @Transactional
    public AccountEntity createAccount(Authentication auth, AccountRequest.CreateRequest request) {

        // userId 추출
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);

        // 설정한 alias 없다면 null
        String accountAlias = request.getAccountAlias();

        // 계좌 번호 생성
        String accountNumber = accountNumberGenerator.generateAccountNumber();

        // 계좌 번호를 비롯한 정보를 AccountEntity 저장
        return accountRepository.save(AccountEntity.builder()
                .userId(userId) // 인증 기능 구현 전까지 '1'로 고정 (기능 구현 후 수정 예정)
                .accountNumber(accountNumber)
                .bankName("ZB_Bank")
                .accountAlias(accountAlias)
                .balance(BigDecimal.valueOf(0))
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public AccountEntity updateAccount(Authentication auth, String accountNumber, AccountRequest.UpdateRequest request) {

        // 계좌 유효성 검증
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountIsClosed(account);

        // 계좌 소유주 검증
        validateAccountAndUserIdMatch(auth, account);

        // 새로운 계좌 번호 생성
        String newAccountNumber = accountNumberGenerator.generateAccountNumber();

        // 변경 사항 적용
        account.setAccountNumber(newAccountNumber);
        account.setAccountAlias(request.getAccountAlias());
        account.setUpdatedAt(LocalDateTime.now());

        return account;
    }

    @Transactional
    public AccountEntity closeAccount(Authentication auth, String accountNumber) {

        // 계좌 유효성 검증
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountIsClosed(account);
        accountValidator.validateBalanceIsZero(account);

        // 계좌 소유주 검증
        validateAccountAndUserIdMatch(auth, account);

        // 계좌 해지 처리
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());

        return account;
    }

    @Transactional
    public AccountEntity addExternalAccount(Authentication auth, AccountRequest.AddRequest request) {

        // userId 추출
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);

        // 입력 받은 계좌 정보
        String bankName = request.getBankName();
        String accountNumber = request.getAccountNumber();
        String accountAlias = request.getAccountAlias();
        BigDecimal balance = request.getBalance();

        // 계좌 추가
        return accountRepository.save(AccountEntity.builder()
                .userId(userId)
                .accountNumber(accountNumber)
                .bankName(bankName)
                .accountAlias(accountAlias)
                .balance(balance)
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public AccountEntity deleteExternalAccount(Authentication auth, String accountNumber) {

        // 계좌 유효성 검증 - 계좌 존재 여부 외 다른 부분은 검증할 필요 없음
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);

        // 계좌 소유주 검증
        validateAccountAndUserIdMatch(auth, account);

        // 계좌 삭제 처리
        account.setAccountStatus(AccountStatus.DELETED);
        account.setClosedAt(LocalDateTime.now());

        return account;
    }

    public List<AccountEntity> showAccountList(Authentication auth, Long userId) {

        // TODO: 인증 작업 필요
        userId = userValidator.findUserByAuthAndGetUserId(auth);

        return accountRepository.findByUserIdAndAccountStatusIn(
                userId,
                Arrays.asList(
                        AccountStatus.ACTIVE,
                        AccountStatus.DORMANT,
                        AccountStatus.SUSPENDED,
                        AccountStatus.INACTIVE)
        );
    }

    // 계좌 소유주 검증
    private void validateAccountAndUserIdMatch(Authentication auth, AccountEntity account) {
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);
        accountValidator.validateAccountUserId(account, userId);
    }
}
