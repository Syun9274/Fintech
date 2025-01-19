package com.zerobase.fintech.common.enums;

public enum AccountStatus {
    ACTIVE,       // 활성화
    DORMANT,      // 휴면 (입출금 제한 또는 해지 전 단계)
    CLOSED,       // 해지 (거래 불가능, 데이터 보관 용도)
    SUSPENDED,    // 일시 정지 (관리자 권한으로 계좌 일시적 제한)
    PENDING,      // 계좌 요청 승인 대기
    INACTIVE,     // 계좌 비활성화 (사용자가 의도적으로 비활성화)
    DELETED,      // 계좌 삭제 (사용자가 등록했던 타 은행 계좌 삭제)
}
