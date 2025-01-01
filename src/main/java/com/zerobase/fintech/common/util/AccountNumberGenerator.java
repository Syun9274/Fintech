package com.zerobase.fintech.common.util;

import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    // 이 서버에서 생성하는 계좌는 전부 `ZB_Bank`의 계좌 번호로 간주 -> 은행 코드(BANK_CODE) 하드 코딩
    private final String BANK_CODE = "468";

    public String generateAccountNumber() {
        return BANK_CODE + String.format("%010d", System.currentTimeMillis());
    }

}
