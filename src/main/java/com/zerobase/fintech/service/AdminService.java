package com.zerobase.fintech.service;

import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.entity.AdminRequestEntity;
import com.zerobase.fintech.repository.AdminRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRequestRepository adminRequestRepository;

    // 대기 중인 요청 기록 불러오기
    public Slice<AdminRequestEntity> showRequestList(Pageable pageable) {
        return adminRequestRepository.findByRequestStatus(RequestStatus.PENDING, pageable);
    }

    // 모든 요청 기록 불러오기
    public Slice<AdminRequestEntity> showAllRequestList(Pageable pageable) {
        return adminRequestRepository.findAll(pageable);
    }
}
