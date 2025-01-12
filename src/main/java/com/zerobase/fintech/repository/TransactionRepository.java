package com.zerobase.fintech.repository;

import com.zerobase.fintech.common.enums.TransactionStatus;
import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    List<TransactionEntity> findByTransactionStatusAndSenderAccountOrReceiverAccount(
            TransactionStatus transactionStatus,
            AccountEntity senderAccount,
            AccountEntity receiverAccount
    );

}
