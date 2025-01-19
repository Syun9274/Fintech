package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.common.enums.RequestType;
import com.zerobase.fintech.controller.dto.request.AdminRequest;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.AccountRequestEntity;
import com.zerobase.fintech.repository.AccountRepository;
import com.zerobase.fintech.repository.AccountRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final AccountRequestRepository accountRequestRepository;

    // 대기 중인 요청 기록 불러오기
    public Slice<AccountRequestEntity> showRequestList(Pageable pageable) {
        return accountRequestRepository.findByRequestStatus(RequestStatus.PENDING, pageable);
    }

    // 모든 요청 기록 불러오기
    public Slice<AccountRequestEntity> showAllRequestList(Pageable pageable) {
        return accountRequestRepository.findAll(pageable);
    }

    @Transactional
    public void approveRequest(Long requestId) {
        AccountRequestEntity requestLog = getRequestById(requestId);

        AccountEntity account = accountRepository.findById(requestLog.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + requestLog.getAccountId()));

        RequestType requestType = requestLog.getRequestType();
        requestType.process(account, accountService);

        requestLog.setRequestStatus(RequestStatus.APPROVED);
    }

    @Transactional
    public void rejectRequest(Long requestId, AdminRequest.RejectRequest request) {
        AccountRequestEntity requestLog = getRequestById(requestId);

        AccountEntity account = accountRepository.findById(requestLog.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + requestLog.getAccountId()));

        if (requestLog.getRequestType() == RequestType.ACCOUNT_CREATION) {
            account.setAccountStatus(AccountStatus.CLOSED);
        } else {
            account.setAccountStatus(AccountStatus.ACTIVE);
        }

        requestLog.setRequestStatus(RequestStatus.REJECTED);
        requestLog.setRejectReason(request.getReason());
    }

    // 요청 ID를 사용하여 AccountRequestEntity 호출
    private AccountRequestEntity getRequestById(Long requestId) {
        return accountRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Request not found: " + requestId));
    }
}
