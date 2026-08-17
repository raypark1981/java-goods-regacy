# AP_CUST_AUTO_CHRG_REQ_DTL (고객 자동충전 신청 내역)

> **테이블명 해석**: `AP`(승인) + `CUST`(고객) + `AUTO`(자동) + `CHRG`(충전) + `REQ`(신청/요청) + `DTL`(상세/내역)  
> **기본 설명**: 승인 업무에서 고객의 자동충전 신청 상세 내역을 관리하는 테이블

> **구분**: 신규 테이블  
> **업무**: PE (선불충전)  
> **용도**: 고객 자동충전 신청 시 1·2순위 계좌 설정 내역 관리  
> **특이사항**: 계획보다 조기 반영. 승인 ON 이후 계좌 순위가 변경되어도 자동충전 1·2순위 계좌는 변하지 않음.

## 컬럼 정의

| 순번 | Key | 컬럼명        | 설명          | Type | Nullable | 비고 |
|------|-----|---------------|---------------|------|----------|------|
| 1 | PK | MCUST_NO      | 통합고객번호  | VARCHAR2(10) | N | |
| 2 | | AUTO_CHRG_YN  | 자동충전여부  | VARCHAR2(1) | N | Y/N |
| 3 | | PTY1_ACNT_SEQ | 1순위계좌순번 | NUMBER | N | 1순위 계좌 |
| 4 | | PTY2_ACNT_SEQ | 2순위계좌순번 | NUMBER | N | 2순위 계좌 |
| 5 | | RGST_ID       | 등록자ID      | VARCHAR2(100) | N | |
| 6 | | RGST_IP       | 등록자IP      | VARCHAR2(50) | N | |
| 7 | | REG_DTM       | 등록일시      | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 8 | | CHGP_ID       | 변경자ID      | VARCHAR2(100) | N | |
| 9 | | CHGP_IP       | 변경자IP      | VARCHAR2(30) | N | |
| 10 | | CHG_DTM       | 변경일시      | VARCHAR2(14) | N | yyyyMMddHHmmss |

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_AUTO_CHRG_REQ_DTL (
    MCUST_NO       VARCHAR2(10)  NOT NULL,  -- 통합고객번호
    AUTO_CHRG_YN   VARCHAR2(1)   NOT NULL,  -- 자동충전여부 (Y/N)
    PTY1_ACNT_SEQ  NUMBER        NOT NULL,  -- 1순위계좌순번
    PTY2_ACNT_SEQ  NUMBER        NOT NULL,  -- 2순위계좌순번
    RGST_ID        VARCHAR2(100) NOT NULL,  -- 등록자ID
    RGST_IP        VARCHAR2(50)  NOT NULL,  -- 등록자IP
    REG_DTM        VARCHAR2(14)  NOT NULL,  -- 등록일시 (yyyyMMddHHmmss)
    CHGP_ID        VARCHAR2(100) NOT NULL,  -- 변경자ID
    CHGP_IP        VARCHAR2(30)  NOT NULL,  -- 변경자IP
    CHG_DTM        VARCHAR2(14)  NOT NULL,  -- 변경일시 (yyyyMMddHHmmss)
    CONSTRAINT PK_AP_CUST_AUTO_CHRG_REQ_DTL PRIMARY KEY (MCUST_NO)
);

COMMENT ON TABLE  AP_CUST_AUTO_CHRG_REQ_DTL               IS '고객 자동충전 신청 내역';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.MCUST_NO      IS '통합고객번호';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.AUTO_CHRG_YN  IS '자동충전여부 (Y/N)';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.PTY1_ACNT_SEQ IS '1순위계좌순번';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.PTY2_ACNT_SEQ IS '2순위계좌순번';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.RGST_ID       IS '등록자ID';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.RGST_IP       IS '등록자IP';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.REG_DTM       IS '등록일시';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.CHGP_ID       IS '변경자ID';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.CHGP_IP       IS '변경자IP';
COMMENT ON COLUMN AP_CUST_AUTO_CHRG_REQ_DTL.CHG_DTM       IS '변경일시';
```

---

## 조회 쿼리 예제

```sql
-- 전체 조회
SELECT * FROM AP_CUST_AUTO_CHRG_REQ_DTL;

-- 특정 회원 조회
SELECT * FROM AP_CUST_AUTO_CHRG_REQ_DTL WHERE MCUST_NO = 'TEST0001';
```
