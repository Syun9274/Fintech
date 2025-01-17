package com.zerobase.fintech.repository;

import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.entity.AdminRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRequestRepository extends JpaRepository<AdminRequestEntity, Long> {

    Slice<AdminRequestEntity> findByRequestStatus(RequestStatus requestStatus, Pageable pageable);
}
