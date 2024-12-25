package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.common.util.AccountNumberGenerator;
import com.zerobase.fintech.controller.dto.request.AccountRequest.createAccountRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    // TODO: 나중에 인증 기능을 구현한 뒤에 userid를 추출 및 계좌 저장 시 적용 필요
    @Transactional
    public AccountEntity createAccount(createAccountRequest request) {

        // 설정한 alias가 없다면 null
        String accountAlias = request.getAccountAlias();

        // 계좌번호 생성
        String accountNumber = accountNumberGenerator.generateAccountNumber();

        // 계좌번호를 비롯한 정보를 AccountEntity에 저장
        return accountRepository.save(AccountEntity.builder()
                .userId(1L) // 인증 기능 구현 전까지 '1'로 고정 (기능 구현 후 수정 예정)
                .accountNumber(accountNumber)
                .bankName("ZB_Bank")
                .accountAlias(accountAlias)
                .balance(BigDecimal.valueOf(0))
                .accountStatus(AccountStatus.ACTIVE)
                .createdAt(java.time.LocalDateTime.now())
                .closedAt(null)
                .build());
    }
}
