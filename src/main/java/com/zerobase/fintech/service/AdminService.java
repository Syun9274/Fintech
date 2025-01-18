package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.entity.AccountRequestEntity;
import com.zerobase.fintech.repository.AccountRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AccountRequestRepository accountRequestRepository;

    // 대기 중인 요청 기록 불러오기
    public Slice<AccountRequestEntity> showRequestList(Pageable pageable) {
        return accountRequestRepository.findByRequestStatus(RequestStatus.PENDING, pageable);
    }

    // 모든 요청 기록 불러오기
    public Slice<AccountRequestEntity> showAllRequestList(Pageable pageable) {
        return accountRequestRepository.findAll(pageable);
    }

    // TODO: 요청 승인 / 거절 로직 작성
}
