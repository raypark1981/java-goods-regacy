# AP_CUST_PNT_AUTO_CHRG_HIS (고객포인트자동충전이력)

> **구분**: 신규 테이블  
> **업무**: PE (선불충전) / AP (승인)  
> **용도**: 고객 포인트 자동충전 실행 이력 및 처리 결과 관리

## 컬럼 정의

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | LOG_ID | 로그ID | VARCHAR2(10) | N | |
| 2 | | EXEC_DTM | 실행일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 3 | | MCUST_NO | 고객번호 | VARCHAR2(10) | N | |
| 6 | | CHRG_TYPE_GBCD | 충전유형구분코드 | VARCHAR2(2) | N | 용어신청 완료. 자동(01)/예약(02) |
| 7 | | RSV_CHRG_MTHD_GBCD | 예약충전방법구분코드 | VARCHAR2(2) | N | 금액부족/지정일 |
| 8 | | RSV_CHRG_PRD_GBCD | 예약충전주기구분코드 | VARCHAR2(2) | N | 매월/매주 |
| 9 | | RSV_CHRG_DT_GBCD | 예약충전일구분코드 | VARCHAR2(2) | Y | 월~금, 1~일 |
| 10 | | RSV_BSIC_AMT_GBCD | 예약기준금액구분코드 | VARCHAR2(2) | Y | 만원~30만원 |
| 11 | | ACNT_CHRG_AMT | 계좌충전금액 | NUMBER | N | |
| 12 | | REAL_CHRG_AMT | 실충전금액 | NUMBER | N | 용어신청 완료 |
| 13 | | ACNT_SEQ | 계좌순번 | NUMBER | N | 고객별 충전계좌 삭제 시 필요 |
| 14 | | BANK_GBCD | 은행구분코드 | VARCHAR2(4) | N | 고객별 충전계좌 삭제 시 필요 |
| 15 | | ACNT_NO | 계좌번호 | VARCHAR2(50) | N | 고객별 충전계좌 삭제 시 필요 |
| 16 | | PROC_RST_GBCD | 처리결과구분코드 | VARCHAR2(2) | N | 성공/실패 |
| 17 | | ERR_CD | 오류코드 | VARCHAR2(10) | N | |
| 18 | | ERR_MSG_CNTN | 오류메시지내용 | VARCHAR2(300) | N | |

> 첨부 이미지의 순번이 3번 다음 6번으로 이어져 4~5번 컬럼은 이미지에서 확인되지 않았다.

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_PNT_AUTO_CHRG_HIS (
    LOG_ID              VARCHAR2(10)  NOT NULL,  -- 로그ID
    EXEC_DTM            VARCHAR2(14)  NOT NULL,  -- 실행일시 (yyyyMMddHHmmss)
    MCUST_NO            VARCHAR2(10)  NOT NULL,  -- 고객번호
    CHRG_TYPE_GBCD      VARCHAR2(2)   NOT NULL,  -- 충전유형구분코드
    RSV_CHRG_MTHD_GBCD  VARCHAR2(2)   NOT NULL,  -- 예약충전방법구분코드
    RSV_CHRG_PRD_GBCD   VARCHAR2(2)   NOT NULL,  -- 예약충전주기구분코드
    RSV_CHRG_DT_GBCD    VARCHAR2(2)   NULL,      -- 예약충전일구분코드
    RSV_BSIC_AMT_GBCD   VARCHAR2(2)   NULL,      -- 예약기준금액구분코드
    ACNT_CHRG_AMT       NUMBER        NOT NULL,  -- 계좌충전금액
    REAL_CHRG_AMT       NUMBER        NOT NULL,  -- 실충전금액
    ACNT_SEQ            NUMBER        NOT NULL,  -- 계좌순번
    BANK_GBCD           VARCHAR2(4)   NOT NULL,  -- 은행구분코드
    ACNT_NO             VARCHAR2(50)  NOT NULL,  -- 계좌번호
    PROC_RST_GBCD       VARCHAR2(2)   NOT NULL,  -- 처리결과구분코드
    ERR_CD              VARCHAR2(10)  NOT NULL,  -- 오류코드
    ERR_MSG_CNTN        VARCHAR2(300) NOT NULL,  -- 오류메시지내용
    CONSTRAINT PK_AP_CUST_PNT_AUTO_CHRG_HIS PRIMARY KEY (LOG_ID)
);

COMMENT ON TABLE  AP_CUST_PNT_AUTO_CHRG_HIS                    IS '고객포인트자동충전이력';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.LOG_ID              IS '로그ID';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.EXEC_DTM            IS '실행일시';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.MCUST_NO            IS '고객번호';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.CHRG_TYPE_GBCD      IS '충전유형구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.RSV_CHRG_MTHD_GBCD  IS '예약충전방법구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.RSV_CHRG_PRD_GBCD   IS '예약충전주기구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.RSV_CHRG_DT_GBCD    IS '예약충전일구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.RSV_BSIC_AMT_GBCD   IS '예약기준금액구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.ACNT_CHRG_AMT       IS '계좌충전금액';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.REAL_CHRG_AMT       IS '실충전금액';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.ACNT_SEQ            IS '계좌순번';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.BANK_GBCD           IS '은행구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.ACNT_NO             IS '계좌번호';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.PROC_RST_GBCD       IS '처리결과구분코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.ERR_CD              IS '오류코드';
COMMENT ON COLUMN AP_CUST_PNT_AUTO_CHRG_HIS.ERR_MSG_CNTN        IS '오류메시지내용';
```
