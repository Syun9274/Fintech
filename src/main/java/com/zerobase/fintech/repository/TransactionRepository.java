package com.zerobase.fintech.repository;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.TransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    Slice<TransactionEntity> findByTransactionStatusAndSenderAccountOrReceiverAccount(
            TransactionStatus transactionStatus,
            AccountEntity senderAccount,
            AccountEntity receiverAccount,
            Pageable pageable
    );

}
