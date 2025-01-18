package com.zerobase.fintech.repository;

import com.zerobase.fintech.common.enums.RequestStatus;
import com.zerobase.fintech.entity.AccountRequestEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRequestRepository extends JpaRepository<AccountRequestEntity, Long> {

    Slice<AccountRequestEntity> findByRequestStatus(RequestStatus requestStatus, Pageable pageable);
}
