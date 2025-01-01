package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.common.util.AccountNumberGenerator;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountValidator accountValidator;

    @Transactional
    public AccountEntity createAccount(AccountRequest.CreateRequest request) {

        // TODO: 나중에 인증 기능을 구현한 뒤에 `userid`를 추출 및 계좌 저장 시 적용 필요

        // 설정한 alias가 없다면 null
        String accountAlias = request.getAccountAlias();

        // 계좌 번호 생성
        String accountNumber = accountNumberGenerator.generateAccountNumber();

        // 계좌 번호를 비롯한 정보를 AccountEntity에 저장
        return accountRepository.save(AccountEntity.builder()
                .userId(1L) // 인증 기능 구현 전까지 '1'로 고정 (기능 구현 후 수정 예정)
                .accountNumber(accountNumber)
                .bankName("ZB_Bank")
                .accountAlias(accountAlias)
                .balance(BigDecimal.valueOf(0))
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public AccountEntity updateAccount(AccountRequest.UpdateRequest request) {

        String accountNumber = request.getAccountNumber();

        // 계좌 유효성 검증
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountNotClosed(account);

        // TODO: 인증 기능을 통해 계좌 소유주인지 확인할 필요 있음


        // 새로운 계좌 번호 생성
        String newAccountNumber = accountNumberGenerator.generateAccountNumber();

        // 변경 사항 적용
        account.setAccountNumber(newAccountNumber);
        account.setUpdatedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Transactional
    public AccountEntity closeAccount(AccountRequest.CloseRequest request) {

        String accountNumber = request.getAccountNumber();

        // 계좌 유효성 검증
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountNotClosed(account);
        accountValidator.validateBalanceIsZero(account);

        // TODO: 인증 기능을 통해 계좌 소유주인지 확인할 필요 있음


        // 계좌 해지 처리
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }

    @Transactional
    public AccountEntity addAccount(AccountRequest.AddRequest request) {

        // TODO: 나중에 인증 기능을 구현한 뒤에 `userid`를 추출 및 계좌 저장 시 적용 필요


        // 입력 받은 계좌 정보
        String bankName = request.getBankName();
        String accountNumber = request.getAccountNumber();
        String accountAlias = request.getAccountAlias();
        BigDecimal balance = request.getBalance();

        // 계좌 추가
        return accountRepository.save(AccountEntity.builder()
                .userId(1L)
                .accountNumber(accountNumber)
                .bankName(bankName)
                .accountAlias(accountAlias)
                .balance(balance)
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public AccountEntity deleteAccount(AccountRequest.DeleteRequest request) {

        String accountNumber = request.getAccountNumber();

        // 계좌 유효성 검증 - 계좌 존재 여부 외 다른 부분은 검증할 필요 없음
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);

        // TODO: 인증 기능을 통해 계좌 소유주인지 확인할 필요 있음


        // 계좌 삭제 처리
        account.setAccountStatus(AccountStatus.DELETED);
        account.setClosedAt(LocalDateTime.now());

        return accountRepository.save(account);
    }
}
