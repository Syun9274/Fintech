package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.common.enums.BankName;
import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.common.enums.RequestType;
import com.zerobase.fintech.common.util.AccountNumberGenerator;
import com.zerobase.fintech.common.util.AccountValidator;
import com.zerobase.fintech.common.util.UserValidator;
import com.zerobase.fintech.controller.dto.request.AccountRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.AccountRequestEntity;
import com.zerobase.fintech.repository.AccountRepository;
import com.zerobase.fintech.repository.AccountRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountRequestRepository accountRequestRepository;

    private final AccountNumberGenerator accountNumberGenerator;
    private final AccountValidator accountValidator;
    private final UserValidator userValidator;

    @Transactional
    public AccountEntity makeCreateAccountRequest(Authentication auth, AccountRequest.CreateRequest request) {

        // userId 추출 및 상태 검증
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);
        userValidator.userStatusIsActive(userId);

        // 설정한 alias 없다면 null
        String accountAlias = request.getAccountAlias();

        // 계좌 번호 생성
        String accountNumber = accountNumberGenerator.generateAccountNumber();

        // 요청 생성
        makeAccountRequest(userId, null, RequestType.ACCOUNT_CREATION);

        // 계좌 번호를 비롯한 정보를 AccountEntity 저장
        return accountRepository.save(AccountEntity.builder()
                .userId(userId)
                .accountNumber(accountNumber)
                .bankName(BankName.ZB_Bank.getName())
                .accountAlias(accountAlias)
                .balance(BigDecimal.valueOf(0))
                .accountStatus(AccountStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void makeUpdateAccountRequest(Authentication auth, String accountNumber, AccountRequest.UpdateRequest request) {

        // 계좌 유효성 검증
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountIsClosed(account);

        // 계좌 소유주 검증
        Long userId = validateAccountAndUserIdMatch(auth, account);

        // 요청 생성
        makeAccountRequest(userId, account.getId(), RequestType.ACCOUNT_RENEWAL);

        // 계좌 임시 비활성화 및 요청 승인 대기 (PENDING)
        account.setAccountStatus(AccountStatus.PENDING);
    }

    @Transactional
    public AccountEntity makeCloseAccountRequest(Authentication auth, String accountNumber) {

        // 계좌 유효성 검증
        accountValidator.validateAccountNumberFormat(accountNumber);
        AccountEntity account = accountValidator.validateAccountExists(accountNumber);
        accountValidator.validateBankName(account);
        accountValidator.validateAccountIsClosed(account);
        accountValidator.validateBalanceIsZero(account);

        // 계좌 소유주 검증
        Long userId = validateAccountAndUserIdMatch(auth, account);

        // 요청 생성
        makeAccountRequest(userId, account.getId(), RequestType.ACCOUNT_CLOSURE);

        // 계좌 임시 비활성화 및 요청 승인 대기 (PENDING)
        account.setAccountStatus(AccountStatus.PENDING);

        return account;
    }

    @Transactional
    public AccountEntity addExternalAccount(Authentication auth, AccountRequest.AddRequest request) {

        // userId 추출 및 상태 검증
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);
        userValidator.userStatusIsActive(userId);

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

        // userId 추출
        Long authUserId = userValidator.findUserByAuthAndGetUserId(auth);

        // 요청된 userId와 추출한 userId 일치 여부 검증
        if (!Objects.equals(authUserId, userId)) {
            throw new IllegalArgumentException("Wrong user id");
        }

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
    private Long validateAccountAndUserIdMatch(Authentication auth, AccountEntity account) {
        Long userId = userValidator.findUserByAuthAndGetUserId(auth);
        accountValidator.validateAccountUserId(account, userId);
        userValidator.userStatusIsActive(userId);

        return userId;
    }

    // 관리자 요청 생성
    @Transactional
    public void makeAccountRequest(Long userId, Long accountId, RequestType requestType) {
        accountRequestRepository.save(AccountRequestEntity.builder()
                .userId(userId)
                .accountId(accountId)
                .requestType(requestType)
                .requestStatus(RequestStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build());
    }

    // 계좌 개설 승인 사항 적용
    @Transactional
    public void createAccount(AccountEntity account) {

        // 계좌 활성화
        account.setAccountStatus(AccountStatus.ACTIVE);
    }

    // 계좌 재발급 사항 적용
    @Transactional
    public void updateAccount(AccountEntity account, String accountAlias) {

        // 별칭 수정 사항이 없는 경우 기존의 별칭 유지
        if (accountAlias == null) {
            accountAlias = account.getAccountAlias();
        }

        String newAccountNumber = accountNumberGenerator.generateAccountNumber();

        // 변경 사항 적용
        account.setAccountNumber(newAccountNumber);
        account.setAccountAlias(accountAlias);
        account.setUpdatedAt(LocalDateTime.now());
    }

    // 계좌 해지 적용
    @Transactional
    public void closeAccount(AccountEntity account) {

        // 계좌 해지 처리
        account.setAccountStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
    }
}
