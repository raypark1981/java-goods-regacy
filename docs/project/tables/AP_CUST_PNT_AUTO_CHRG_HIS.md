# AP_CUST_PNT_AUTO_CHRG_HIS (고객포인트자동충전이력)

> **테이블명 해석**: `AP`(승인) + `CUST`(고객) + `PNT`(포인트) + `AUTO`(자동) + `CHRG`(충전) + `HIS`(이력)  
> **기본 설명**: 승인 업무에서 고객 포인트 자동충전 처리 이력을 관리하는 테이블

> **구분**: 신규 테이블  
> **업무**: PE (선불충전) / AP (승인)  
> **용도**: 고객 포인트 자동충전 실행 이력 및 처리 결과 관리

## 컬럼 정의

> **2026-08-18 갱신**: 실제 DB(DB 클라이언트 조회 캡처, 컬럼명은 직접 확인 완료) 기준으로 재정리. 실제 테이블은 `LOG_ID`/`EXEC_DTM`/`MCUST_NO`를 제외한 전 컬럼이 `Nullable=Y`로 배포되어 있음(이전 설계안의 NOT NULL 다수와 다름).

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | LOG_ID | 로그ID | NUMBER | N | |
| 2 | | EXEC_DTM | 실행일시 | VARCHAR2(14 BYTE) | N | yyyyMMddHHmmss |
| 3 | | MCUST_NO | 통합회원번호 | VARCHAR2(10 BYTE) | N | |
| 4 | | ORD_ID | 주문ID | VARCHAR2(50 BYTE) | Y | 확인 완료 |
| 5 | | PAY_KEY | 결제KEY | VARCHAR2(128 BYTE) | Y | 토스 paymentKey. 기존 문서엔 `PAYMENT_KEY`로 잘못 기재돼 있었음 |
| 6 | | CHRG_TYPE_GBCD | 충전유형구분코드 | VARCHAR2(2 BYTE) | Y | 자동(01)/예약(02) |
| 7 | | RSV_CHRG_MTHD_GBCD | 예약충전방법구분코드 | VARCHAR2(2 BYTE) | Y | 금액부족/지정일 |
| 8 | | RSV_CHRG_PRD_GBCD | 예약충전주기구분코드 | VARCHAR2(2 BYTE) | Y | 매월/매주 |
| 9 | | RSV_CHRG_DLU_GBCD | 예약충전일구분코드 | VARCHAR2(2 BYTE) | Y | 확인 완료. 기존 문서엔 `RSV_CHRG_DT_GBCD`로 잘못 기재돼 있었음 |
| 10 | | RSV_BSIC_AMT_GBCD | 예약기준금액구분코드 | VARCHAR2(2 BYTE) | Y | |
| 11 | | ACNT_CHRG_AMT | 계좌충전금액 | NUMBER | Y | |
| 12 | | REAL_CHRG_AMT | 실충전금액 | NUMBER | Y | |
| 13 | | ACNT_SEQ | 계좌순번 | NUMBER | Y | |
| 14 | | BANK_GBCD | 은행구분코드 | VARCHAR2(4 BYTE) | Y | |
| 15 | | ACNT_NO | 계좌번호 | VARCHAR2(50 BYTE) | Y | |
| 16 | | PROC_RST_GBCD | 처리결과구분코드 | VARCHAR2(2 BYTE) | Y | 성공(01)/실패(02) |
| 17 | | ERR_CD | 오류코드 | VARCHAR2(10 BYTE) | Y | |
| 18 | | ERR_MSG_CNTN | 오류메시지내용 | VARCHAR2(300 BYTE) | Y | |
| 19 | | BANK_ACNT_NM | 은행계좌이름 | VARCHAR2(20 BYTE) | Y | `insertAutoChrgHis` 코드에 반영 완료 |

---

## CREATE TABLE (실제 배포된 테이블 기준, 2026-08-18 갱신)

> 아래는 실제 DB 컬럼 그대로 재구성한 것 (컬럼명 확인 완료).

```sql
CREATE TABLE AP_CUST_PNT_AUTO_CHRG_HIS (
    LOG_ID              NUMBER         NOT NULL,  -- 로그ID
    EXEC_DTM            VARCHAR2(14)   NOT NULL,  -- 실행일시 (yyyyMMddHHmmss)
    MCUST_NO            VARCHAR2(10)   NOT NULL,  -- 통합회원번호
    ORD_ID              VARCHAR2(50)   NULL,      -- 주문ID
    PAY_KEY             VARCHAR2(128)  NULL,      -- 결제KEY (토스 paymentKey)
    CHRG_TYPE_GBCD      VARCHAR2(2)    NULL,      -- 충전유형구분코드
    RSV_CHRG_MTHD_GBCD  VARCHAR2(2)    NULL,      -- 예약충전방법구분코드
    RSV_CHRG_PRD_GBCD   VARCHAR2(2)    NULL,      -- 예약충전주기구분코드
    RSV_CHRG_DLU_GBCD   VARCHAR2(2)    NULL,      -- 예약충전일구분코드
    RSV_BSIC_AMT_GBCD   VARCHAR2(2)    NULL,      -- 예약기준금액구분코드
    ACNT_CHRG_AMT       NUMBER         NULL,      -- 계좌충전금액
    REAL_CHRG_AMT       NUMBER         NULL,      -- 실충전금액
    ACNT_SEQ            NUMBER         NULL,      -- 계좌순번
    BANK_GBCD           VARCHAR2(4)    NULL,      -- 은행구분코드
    ACNT_NO             VARCHAR2(50)   NULL,      -- 계좌번호
    PROC_RST_GBCD       VARCHAR2(2)    NULL,      -- 처리결과구분코드
    ERR_CD              VARCHAR2(10)   NULL,      -- 오류코드
    ERR_MSG_CNTN        VARCHAR2(300)  NULL,      -- 오류메시지내용
    BANK_ACNT_NM        VARCHAR2(20)   NULL,      -- 은행계좌이름
    CONSTRAINT PK_AP_CUST_PNT_AUTO_CHRG_HIS PRIMARY KEY (LOG_ID)
);

-- LOG_ID 채번용 시퀀스
CREATE SEQUENCE AP_CUST_PNT_AUTO_CHRG_HIS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE;
```

> **2026-08-18 변경**: `LOG_ID` 채번을 시퀀스가 아니라 **`INSERT` 문 안의 서브쿼리로 `MAX(LOG_ID)+1`을 계산**하는 방식으로 변경 (`(SELECT NVL(MAX(LOG_ID), 0) + 1 FROM AP_CUST_PNT_AUTO_CHRG_HIS)`). Java에서 별도 SELECT 후 INSERT하는 방식(왕복 갭 존재)이 아니라 INSERT 시점에 DB가 한 번에 계산하도록 해서 동시성 위험을 줄였다. 단, 시퀀스처럼 완전히 동시성 안전하진 않음(같은 순간 커밋 전 상태를 동시에 읽으면 여전히 PK 충돌 가능). 이에 따라 `AP_CUST_PNT_AUTO_CHRG_HIS_SEQ` 시퀀스는 더 이상 사용하지 않는다.

---

## 조회 쿼리 예제

```sql
-- 전체 조회
SELECT * FROM AP_CUST_PNT_AUTO_CHRG_HIS;

-- 특정 회원의 충전 이력 (최신순)
SELECT * FROM AP_CUST_PNT_AUTO_CHRG_HIS WHERE MCUST_NO = 'TEST0001' ORDER BY EXEC_DTM DESC;
```

---

## 토스 빌링 결제 응답(Response) 필드별 저장 여부

> 자동충전 결제 테스트 시 토스로부터 수신한 실제 응답값 기준. 현재 `requestAutoCharge`에서 이 응답을 `AP_CUST_PNT_AUTO_CHRG_HIS`에 저장하는 로직은 미구현 상태 — 저장 로직 추가 시 이 표를 기준으로 컬럼을 설계한다.

| 필드 | 값 | 저장필수여부 | 이유 |
|---|---|---|---|
| paymentKey | tviva20260816132510h1Rb1 | Y | 결제 취소/조회 시 토스 API 호출에 필수 키 (`/v1/payments/{paymentKey}/cancel`) |
| orderId | AUTO_CHARGE_TEST0001_20260816132358 | Y | 우리 쪽 주문과 매칭하는 키 |
| status | DONE | Y | 결제 상태값. `PROC_RST_GBCD`(처리결과구분코드)에 매핑 |
| totalAmount | 30000 | Y | 실제 승인된 충전금액. `REAL_CHRG_AMT`에 매핑 |
| approvedAt | 2026-08-16T13:25:10+09:00 | Y | 승인시각. 이력/정산 조회 기준 시각 |
| transfer.bankCode | 90 | Y | 은행구분코드. `BANK_GBCD`와 매칭 |
| lastTransactionKey | txrd_a01m04d20gqz7kktxw7em1pcftq | Y | 부분취소 등 후속 처리 시 필요 |
| mId | tvivarepublica2 | N | 가맹점 ID. 대사(정산) 참고용, 필수 아님 |
| receipt.url | https://dashboard-sandbox.tosspayments.com/receipt/redirection?... | N | 고객 영수증 안내 화면에 쓸 경우만 필요 |
| cashReceipt.receiptUrl | https://dashboard-sandbox.tosspayments.com/receipts/cash-receipt/... | N | 현금영수증 안내 화면에 쓸 경우만 필요 |
| cashReceipt.issueNumber | 730001091 | N | 현금영수증 발급번호. 안내 목적 외엔 불필요 |
| method | 계좌이체 | N | 이미 `BANK_GBCD` 등으로 결제수단을 알고 있어 중복 |
| transfer.settlementStatus | INCOMPLETED | N | 시간 경과에 따라 변하는 값이라 이력 스냅샷엔 부적합 |
| suppliedAmount | 27273 | N | 세금계산서 발행 등 별도 요건 없으면 불필요 |
| vat | 2727 | N | 세금계산서 발행 등 별도 요건 없으면 불필요 |
| requestedAt | 2026-08-16T13:25:10+09:00 | N | approvedAt과 사실상 동일 시각, 중복 |
| country | KR | N | 항상 고정값 |
| currency | KRW | N | 항상 고정값 |
| version | 2024-06-01 | N | 토스 API 버전 정보, 비즈니스 로직에 불필요 |
| checkout.url | https://api.tosspayments.com/v1/payments/.../checkout | N | 결제 전 단계용 URL, 완료 건엔 무의미 |
| cultureExpense | false | N | 실물상품용 필드, 포인트 충전과 무관 |
| useEscrow | false | N | 에스크로 상품용 필드, 포인트 충전과 무관 |
| isPartialCancelable | true | N | 정책상 항상 동일, 저장 실익 없음 |
| taxExemptionAmount | 0 | N | 포인트 충전엔 해당 없음 |
| taxFreeAmount | 0 | N | 포인트 충전엔 해당 없음 |
| cashReceipt.amount | 30000 | N | totalAmount와 동일값, 중복 |
| cashReceipt.taxFreeAmount | 0 | N | 포인트 충전엔 해당 없음 |
| cashReceipt.type | 소득공제 | N | 고정값(소득공제), 저장 실익 없음 |
| statusCode | 200 | N | status(DONE)로 충분, HTTP 코드 별도 저장 불필요 |
| type | BILLING | N | 항상 고정값(빌링 결제) |
| metadata / cashReceipts / discount / virtualAccount / easyPay / cancels / mobilePhone / failure / giftCertificate / card | null | N | 해당 케이스 아님 (모두 null) |
| **secret** | ps_kYG57Eba3Gmb5RYk2q0zrpWDOxmA | **금지** | 웹훅 서명 검증용 비밀값. DB/로그 어디에도 저장하면 안 됨 |
| cashReceipt.receiptKey | xLpgeoOn4d26DlbXAaV0dyWaM7RjwqVqY50Q9RBKEzMjPJyG | N | 현금영수증 취소 API 호출 시에만 필요, 현재 미구현 기능 |

---

## 토스 결제 취소 응답(Response) 필드별 저장 여부

> 자동충전 결제 취소 테스트 시 토스로부터 수신한 실제 응답값 기준. 현재 `cancelAutoCharge`는 `paymentKey`, `orderId`, `status`만 이력에 반영하고 있고, `cancels[0]` 하위 필드(취소금액/취소시각/취소거래키)는 아직 저장하지 않는 상태 — 저장하려면 `AP_CUST_PNT_AUTO_CHRG_HIS`에 컬럼 추가가 필요하다.

| 필드 | 값 | 저장필수여부 | 이유 |
|---|---|---|---|
| paymentKey | tviva20260816224847g73F5 | 필수 저장값 | 원본 충전 건과 매칭하는 키 (이미 저장 중) |
| orderId | AUTO_CHARGE_TEST0001_20260816224846 | 필수 저장값 | 원본 충전 건과 매칭하는 키 (이미 저장 중) |
| status | CANCELED | 필수 저장값 | 처리결과. `PROC_RST_GBCD`(취소="03")에 매핑 (이미 저장 중) |
| cancels[0].cancelAmount | 30000 | 필수 저장값 | 실제 취소된 금액. 부분취소 대응 시 꼭 필요 (현재 미저장) |
| cancels[0].canceledAt | 2026-08-17T15:20:48+09:00 | 필수 저장값 | 취소 처리 시각. 지금은 `EXEC_DTM`(취소 API 호출 시각)으로 대체 중이라 토스 측 실제 취소 완료 시각과는 다를 수 있음 |
| cancels[0].transactionKey | txrd_a01m0762ftgxghxgvh3kge5600s | 필수 저장값 | 이 취소 건 자체의 거래키. 재조회/이중취소 방지 추적용 (현재 미저장) |
| cancels[0].cancelStatus | DONE | N | status(CANCELED)와 사실상 같은 정보, 중복 |
| cancels[0].cancelReason | 자동충전 결제 취소 | N | 우리가 보낸 값이 그대로 돌아온 것, 이미 알고 있음 |
| balanceAmount | 0 | N | totalAmount - cancelAmount로 계산 가능, 중복 |
| lastTransactionKey | txrd_a01m0762ftgxghxgvh3kge5600s | N | `cancels[0].transactionKey`와 동일값, 중복 |
| totalAmount | 30000 | N | 원본 충전 이력(`ACNT_CHRG_AMT`)에 이미 저장돼 있음, 중복 |
| approvedAt / requestedAt | 2026-08-16T22:48:47+09:00 | N | 원본 충전 시각 그대로 돌아온 것, 원본 이력에 이미 있음 |
| cashReceipts[0].receiptUrl | https://dashboard-sandbox.tosspayments.com/receipts/cash-receipt/... | N | 취소 현금영수증 안내 화면에 쓸 경우만 필요 |
| cashReceipts[0].issueStatus | IN_PROGRESS | N | 상태값, 필요하면 조회 API로 재확인 가능 |
| cashReceipts[0].transactionType | CANCEL | N | 이미 우리 쪽 `PROC_RST_GBCD`로 취소 여부를 알고 있어 중복 |
| country / currency / version / type / mId / method / checkout.url / cultureExpense / useEscrow / isPartialCancelable / taxExemptionAmount / taxFreeAmount / suppliedAmount / vat / transfer / statusCode | - | N | 이전 충전 응답 정리 때와 동일한 이유로 불필요 (고정값·중복·해당없음) |
| metadata / discount / virtualAccount / easyPay / mobilePhone / failure / giftCertificate / card | null | N | 해당 케이스 아님 (모두 null) |
| cashReceipt(원본, 배열 아닌 단수형) | - | N | 원본 충전 응답 정리 때 이미 다룬 값, 취소 응답에도 그대로 따라옴 |
| **secret** | ps_QbgMGZzorznaY01GmmjPrl5E1em4 | **금지** | 웹훅 서명 검증용 비밀값. DB/로그 어디에도 저장하면 안 됨 |
