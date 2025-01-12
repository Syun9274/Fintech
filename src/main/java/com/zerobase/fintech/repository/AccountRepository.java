package com.zerobase.fintech.repository;

import com.zerobase.fintech.common.enums.AccountStatus;
import com.zerobase.fintech.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    List<AccountEntity> findByUserIdAndAccountStatusIn(Long userId, List<AccountStatus> statuses);
}
