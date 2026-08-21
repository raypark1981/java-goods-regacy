# AP_CUST_CHRG_ACNT_MNG (고객별 충전 계좌 관리)

> **테이블명 해석**: `AP`(승인) + `CUST`(고객) + `CHRG`(충전) + `ACNT`(계좌) + `MNG`(관리)  
> **기본 설명**: 승인 업무에서 고객의 충전 계좌 정보를 관리하는 테이블

> **구분**: 신규 테이블  
> **업무**: PE (선불충전)  
> **용도**: 고객이 자동충전에 등록한 계좌 목록 관리

## 컬럼 정의

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | MCUST_NO | 통합고객번호 | VARCHAR2(10) | N | |
| 2 | PK | ACNT_SEQ | 계좌 순번 | NUMBER | N | |
| 3 | | CUST_KEY | 고객KEY | VARCHAR2(128) | N | UUID, 용어신청 완료 |
| 4 | | BILL_KEY | 계산KEY | VARCHAR2(128) | N | 용어신청 완료 |
| 5 | | BANK_GBCD | 은행구분코드 | VARCHAR2(4) | N | 노스 은행코드 |
| 6 | | BANK_ACNT_NM | 은행계좌이름 | VARCHAR2(20) | Y | |
| 7 | | ACNT_NO | 계좌번호 | VARCHAR2(50) | Y | |
| 8 | | LVL_PRTY | 레벨우선순위 | NUMBER | Y | 계좌 우선순위 |
| 9 | | RGST_ID | 등록자ID | VARCHAR2(100) | N | |
| 10 | | RGST_IP | 등록자IP | VARCHAR2(50) | N | |
| 11 | | REG_DTM | 등록일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 12 | | CHGP_ID | 변경자ID | VARCHAR2(100) | N | |
| 13 | | CHGP_IP | 변경자IP | VARCHAR2(30) | N | |
| 14 | | CHG_DTM | 변경일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_CHRG_ACNT_MNG (
    MCUST_NO      VARCHAR2(10)  NOT NULL,  -- 통합고객번호
    ACNT_SEQ      NUMBER        NOT NULL,  -- 계좌 순번
    CUST_KEY      VARCHAR2(128) NOT NULL,  -- 고객KEY (UUID)
    BILL_KEY      VARCHAR2(128) NOT NULL,  -- 계산KEY
    BANK_GBCD     VARCHAR2(4)   NOT NULL,  -- 은행구분코드 (노스 은행코드)
    BANK_ACNT_NM  VARCHAR2(20)  NULL,      -- 은행계좌이름
    ACNT_NO       VARCHAR2(50)  NULL,      -- 계좌번호
    LVL_PRTY      NUMBER        NULL,      -- 계좌 우선순위
    RGST_ID       VARCHAR2(100) NOT NULL,  -- 등록자ID
    RGST_IP       VARCHAR2(50)  NOT NULL,  -- 등록자IP
    REG_DTM       VARCHAR2(14)  NOT NULL,  -- 등록일시 (yyyyMMddHHmmss)
    CHGP_ID       VARCHAR2(100) NOT NULL,  -- 변경자ID
    CHGP_IP       VARCHAR2(30)  NOT NULL,  -- 변경자IP
    CHG_DTM       VARCHAR2(14)  NOT NULL,  -- 변경일시 (yyyyMMddHHmmss)
    CONSTRAINT PK_AP_CUST_CHRG_ACNT_MNG PRIMARY KEY (MCUST_NO, ACNT_SEQ)
);

COMMENT ON TABLE  AP_CUST_CHRG_ACNT_MNG             IS '고객별 충전 계좌 관리';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.MCUST_NO     IS '통합고객번호';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.ACNT_SEQ     IS '계좌 순번';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.CUST_KEY     IS '고객KEY (UUID)';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.BILL_KEY     IS '계산KEY';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.BANK_GBCD    IS '은행구분코드';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.BANK_ACNT_NM IS '은행계좌이름';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.ACNT_NO      IS '계좌번호';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.LVL_PRTY     IS '계좌 우선순위';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.RGST_ID      IS '등록자ID';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.RGST_IP      IS '등록자IP';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.REG_DTM      IS '등록일시';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.CHGP_ID      IS '변경자ID';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.CHGP_IP      IS '변경자IP';
COMMENT ON COLUMN AP_CUST_CHRG_ACNT_MNG.CHG_DTM      IS '변경일시';

```

> **2026-08-18 변경**: `ACNT_SEQ`는 전역 시퀀스가 아니라 **고객(MCUST_NO)별로 1부터 증가**하는 값으로 변경. `insertChrgAcnt` 호출 전 `selectMaxAcntSeq(mcustNo)`로 해당 고객의 현재 최대값을 조회해서 +1한 값을 넣는다 (없으면 1). 이에 따라 `AP_CUST_CHRG_ACNT_MNG_SEQ` 시퀀스는 더 이상 사용하지 않는다.

---

## 조회 쿼리 예제

```sql
-- 전체 조회
SELECT * FROM AP_CUST_CHRG_ACNT_MNG;

-- 특정 회원의 등록 계좌 목록 (우선순위순)
SELECT * FROM AP_CUST_CHRG_ACNT_MNG WHERE MCUST_NO = 'TEST0001' ORDER BY LVL_PRTY ASC;
```
