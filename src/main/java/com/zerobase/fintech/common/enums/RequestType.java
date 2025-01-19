package com.zerobase.fintech.common.enums;

import com.zerobase.fintech.entity.AccountEntity;
import com.zerobase.fintech.service.AccountService;

public enum RequestType {

    ACCOUNT_CREATION {
        @Override
        public void process(AccountEntity account, AccountService accountService) {
            accountService.createAccount(account);
            account.setAccountStatus(AccountStatus.ACTIVE);
        }
    },
    ACCOUNT_RENEWAL {
        @Override
        public void process(AccountEntity account, AccountService accountService) {
            accountService.updateAccount(account);
            account.setAccountStatus(AccountStatus.ACTIVE);
        }
    },
    ACCOUNT_CLOSURE {
        @Override
        public void process(AccountEntity account, AccountService accountService) {
            accountService.closeAccount(account);
            account.setAccountStatus(AccountStatus.CLOSED);
        }
    };

    public abstract void process(AccountEntity account, AccountService accountService);
}
