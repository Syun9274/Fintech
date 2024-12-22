Table User {
  id BIGINT [pk, increment] // 고유 사용자 ID
  email VARCHAR(255) [unique, not null] // 이메일
  password VARCHAR(255) [not null] // 해싱된 비밀번호
  name VARCHAR(100) [not null] // 사용자 이름
  phone_number VARCHAR(15) // 휴대폰 번호
  role ENUM('USER', 'ADMIN') [default: 'USER'] // 사용자 권한
  created_at DATETIME [default: `CURRENT_TIMESTAMP`] // 생성일
  deleted_at DATETIME // 탈퇴 요청일 (NULL이면 활성 상태)
}

Table Account {
  id BIGINT [pk, increment] // 고유 계좌 ID
  user_id BIGINT [not null, ref: > User.id] // 사용자 ID (외래 키)
  account_number VARCHAR(50) [unique, not null] // 계좌 번호
  bank_name VARCHAR(100) [not null] // 은행 이름
  account_alias VARCHAR(50) // 계좌 별칭
  balance BIGINT [default: 0] // 계좌 잔액
  account_status ENUM('ACTIVE', 'CLOSED') [default: 'ACTIVE'] // 계좌 상태
  created_at DATETIME [default: `CURRENT_TIMESTAMP`] // 생성일
  closed_at DATETIME // 해지 요청일
}

Table Transaction {
  id BIGINT [pk, increment] // 고유 거래 ID
  sender_account_id BIGINT [ref: > Account.id] // 송금자 계좌 ID (외래 키)
  receiver_account_id BIGINT [ref: > Account.id] // 수신자 계좌 ID (외래 키)
  transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'TRANSFER') [not null] // 거래 유형
  transaction_result_type ENUM('SUCCESS', 'FAILED') [default: 'SUCCESS'] // 거래 결과
  amount BIGINT [not null] // 거래 금액
  balance_snapshot BIGINT [not null] // 거래 후 잔액 스냅샷
  memo VARCHAR[255] // 메모
  transacted_at DATETIME [default: `CURRENT_TIMESTAMP`] // 거래 시간
}

Table AccountRequest {
  id BIGINT [pk, increment] // 고유 요청 ID
  user_id BIGINT [not null, ref: > User.id] // 요청한 사용자 ID (외래 키)
  account_id BIGINT [ref: > Account.id] // 요청 대상 계좌 ID (외래 키, NULL 가능)
  request_type ENUM('ACCOUNT_CREATION', 'ACCOUNT_CLOSURE', 'ACCOUNT_RENEWAL') [not null] // 요청 유형
  status ENUM('PENDING', 'APPROVED', 'REJECTED') [not null] // 요청 상태
  created_at DATETIME [default: `CURRENT_TIMESTAMP`] // 요청 생성 시간
  processed_at DATETIME // 요청 처리 시간
  rejection_reason VARCHAR(255) // 거절 사유 (선택)
}