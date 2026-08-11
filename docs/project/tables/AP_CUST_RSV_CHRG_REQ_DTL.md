# AP_CUST_RSV_CHRG_REQ_DTL (고객 예약 충전 신청 내역)

> **구분**: 신규 테이블  
> **업무**: PE (선불충전)  
> **용도**: 고객 예약 충전 신청 조건 및 계좌 충전 금액 관리

## 컬럼 정의

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | MCUST_NO | 통합고객번호 | VARCHAR2(10) | N | |
| 2 | PK | SEQ_NO | 순번 | NUMBER | N | |
| 3 | | PNT_ACNT_GCD | 선불계좌구분코드 | NUMBER | Y | 사진 판독 기준 |
| 4 | | PNT_ACNT_SNO | 선불계좌순번 | NUMBER | Y | 사진 판독 기준 |
| 5 | | RSV_CHRG_MTHD_GBCD | 예약충전방법구분코드 | VARCHAR2(2) | N | |
| 6 | | RSV_CHRG_PRD_GCD | 예약충전기간구분코드 | VARCHAR2(2) | Y | |
| 7 | | RSV_ORG_DUJ_GBCD | 예약충전기준구분코드 | VARCHAR2(2) | Y | |
| 8 | | RSV_CHRG_DUJ | 예약충전일 | VARCHAR2(2) | Y | |
| 9 | | RSV_BSC_AMT_GBCD | 예약기준금액구분코드 | VARCHAR2(2) | Y | |
| 10 | | RSV_BSC_AMT | 예약기준금액 | NUMBER | N | |
| 11 | | ACNT_CHRG_AMT | 계좌충전금액 | NUMBER | N | |
| 12 | | DSL_YN | 삭제여부 | VARCHAR2(1) | Y | Y/N |
| 13 | | DEL_DTM | 삭제일시 | VARCHAR2(14) | Y | yyyyMMddHHmmss |
| 14 | | RGST_ID | 등록자ID | VARCHAR2(100) | N | |
| 15 | | RGST_IP | 등록자IP | VARCHAR2(50) | N | |
| 16 | | REG_DTM | 등록일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 17 | | CHGP_ID | 변경자ID | VARCHAR2(100) | N | |
| 18 | | CHGP_IP | 변경자IP | VARCHAR2(50) | N | |
| 19 | | CHG_DTM | 변경일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |

> 첨부 이미지 해상도상 3~9번 일부 컬럼명/설명은 사진 판독 기준으로 정리했다.

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_RSV_CHRG_REQ_DTL (
    MCUST_NO            VARCHAR2(10)  NOT NULL,  -- 통합고객번호
    SEQ_NO              NUMBER        NOT NULL,  -- 순번
    PNT_ACNT_GCD        NUMBER        NULL,      -- 선불계좌구분코드
    PNT_ACNT_SNO        NUMBER        NULL,      -- 선불계좌순번
    RSV_CHRG_MTHD_GBCD  VARCHAR2(2)   NOT NULL,  -- 예약충전방법구분코드
    RSV_CHRG_PRD_GCD    VARCHAR2(2)   NULL,      -- 예약충전기간구분코드
    RSV_ORG_DUJ_GBCD    VARCHAR2(2)   NULL,      -- 예약충전기준구분코드
    RSV_CHRG_DUJ        VARCHAR2(2)   NULL,      -- 예약충전일
    RSV_BSC_AMT_GBCD    VARCHAR2(2)   NULL,      -- 예약기준금액구분코드
    RSV_BSC_AMT         NUMBER        NOT NULL,  -- 예약기준금액
    ACNT_CHRG_AMT       NUMBER        NOT NULL,  -- 계좌충전금액
    DSL_YN              VARCHAR2(1)   NULL,      -- 삭제여부 (Y/N)
    DEL_DTM             VARCHAR2(14)  NULL,      -- 삭제일시 (yyyyMMddHHmmss)
    RGST_ID             VARCHAR2(100) NOT NULL,  -- 등록자ID
    RGST_IP             VARCHAR2(50)  NOT NULL,  -- 등록자IP
    REG_DTM             VARCHAR2(14)  NOT NULL,  -- 등록일시 (yyyyMMddHHmmss)
    CHGP_ID             VARCHAR2(100) NOT NULL,  -- 변경자ID
    CHGP_IP             VARCHAR2(50)  NOT NULL,  -- 변경자IP
    CHG_DTM             VARCHAR2(14)  NOT NULL,  -- 변경일시 (yyyyMMddHHmmss)
    CONSTRAINT PK_AP_CUST_RSV_CHRG_REQ_DTL PRIMARY KEY (MCUST_NO, SEQ_NO)
);

COMMENT ON TABLE  AP_CUST_RSV_CHRG_REQ_DTL                    IS '고객 예약 충전 신청 내역';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.MCUST_NO            IS '통합고객번호';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.SEQ_NO              IS '순번';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.PNT_ACNT_GCD        IS '선불계좌구분코드';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.PNT_ACNT_SNO        IS '선불계좌순번';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_CHRG_MTHD_GBCD  IS '예약충전방법구분코드';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_CHRG_PRD_GCD    IS '예약충전기간구분코드';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_ORG_DUJ_GBCD    IS '예약충전기준구분코드';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_CHRG_DUJ        IS '예약충전일';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_BSC_AMT_GBCD    IS '예약기준금액구분코드';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RSV_BSC_AMT         IS '예약기준금액';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.ACNT_CHRG_AMT       IS '계좌충전금액';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.DSL_YN              IS '삭제여부';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.DEL_DTM             IS '삭제일시';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RGST_ID             IS '등록자ID';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.RGST_IP             IS '등록자IP';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.REG_DTM             IS '등록일시';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.CHGP_ID             IS '변경자ID';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.CHGP_IP             IS '변경자IP';
COMMENT ON COLUMN AP_CUST_RSV_CHRG_REQ_DTL.CHG_DTM             IS '변경일시';
```
