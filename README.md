# 🏦 핀테크 프로젝트
사용자들과 금융 정보를 공유하고 거래 내역을 관리할 수 있는 핀테크 플랫폼입니다.

---

# 프로젝트 기능 및 설계

## 회원 관리 기능

### 회원가입
- 사용자는 이메일, 비밀번호, 이름, 휴대폰 번호를 입력하여 회원가입할 수 있습니다.
- 비밀번호는 반드시 해싱 처리되어 저장됩니다.
- 사용자 가입 시 USER 권한이 기본 부여됩니다.

### 로그인
- 사용자는 이메일과 비밀번호로 로그인할 수 있습니다.
- 로그인 성공 시 JWT 토큰이 발급됩니다.

## 계좌 관리 기능

### 계좌 등록
- 로그인한 사용자는 자신의 은행 계좌를 등록할 수 있습니다.
- 계좌 등록 시 은행명, 계좌 번호, 계좌 별칭을 입력합니다.
- 계좌 번호는 중복되지 않아야 합니다.

### 계좌 목록 조회
- 로그인한 사용자는 자신의 계좌 목록을 확인할 수 있습니다.
- 계좌 정보에는 은행명, 계좌 번호(마스킹 처리), 계좌 별칭, 현재 잔액 정보가 포함됩니다.

## 거래 관리 기능

### 거래 내역 조회
- 로그인한 사용자는 자신의 계좌에서 이루어진 거래 내역을 조회할 수 있습니다.
- 거래 내역에는 거래 금액, 거래 유형(입금/출금), 거래 일시, 거래 상대방 정보가 포함됩니다.
- 거래 내역은 기본적으로 최신순으로 정렬되며, 페이징 처리됩니다.

### 거래 기록 검색
- 사용자는 거래 내역을 거래 유형(입금/출금) 또는 특정 기간 기준으로 검색할 수 있습니다.

### 송금 기능
- 사용자는 등록된 계좌를 통해 다른 계좌로 송금을 진행할 수 있습니다.
- 송금 시 수신 계좌, 송금 금액, 메모를 입력합니다.
- 송금 후 거래 내역이 자동으로 생성됩니다.

## 공지사항 및 알림 기능

### 공지사항 게시글
- 관리자는 금융 관련 공지사항을 게시할 수 있습니다.
- 로그인하지 않은 사용자도 공지사항을 열람할 수 있습니다.

### 실시간 알림
- 거래 발생 시 푸시 알림을 통해 거래 완료 알림이 제공됩니다.

## 보안 기능

### 비밀번호 재설정
- 사용자는 비밀번호를 분실했을 경우 이메일을 통해 재설정할 수 있습니다.

### 이중 인증
- 로그인 시 이메일 OTP(One-Time Password)를 통한 2단계 인증 기능이 제공됩니다.

### 계좌 비밀번호 암호화
- 사용자가 설정한 계좌 비밀번호는 안전하게 암호화됩니다.

---

## ERD
![ERD](doc/img/erd.png)

---

## Trouble Shooting
[go to the trouble shooting section](doc/TROUBLE_SHOOTING.md)

---

## Tech Stack
<div align=center> 
    <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
    <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
    <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
    <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"> 
</div>