# AP_CUST_PNT_PRTY_CHG_HIS (포인트 우선순위 변경 이력)

> **테이블명 해석**: `AP`(승인) + `CUST`(고객) + `PNT`(포인트) + `PRTY`(우선순위) + `CHG`(변경) + `HIS`(이력)  
> **기본 설명**: 승인 업무에서 고객 포인트 우선순위 변경 이력을 관리하는 테이블

> **구분**: 신규 테이블  
> **업무**: AP (승인)  
> **용도**: 고객 포인트 유형 우선순위 변경 이력 조회

## 컬럼 정의

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | MCUST_NO | 통합고객번호 | VARCHAR2(10) | N | |
| 2 | PK | SEQ | 순번 | NUMBER | N | |
| 3 | | PNT_TYPE_GBCD | 포인트유형구분코드 | VARCHAR2(2) | N | 용어신청 완료 |
| 4 | | RGST_ID | 등록자ID | VARCHAR2(100) | N | |
| 5 | | RGST_IP | 등록자IP | VARCHAR2(50) | N | |
| 6 | | REG_DTM | 등록일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 7 | | CHGP_ID | 변경자ID | VARCHAR2(100) | N | |
| 8 | | CHGP_IP | 변경자IP | VARCHAR2(30) | N | |
| 9 | | CHG_DTM | 변경일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_PNT_PRTY_CHG_HIS (
    MCUST_NO       VARCHAR2(10)  NOT NULL,  -- 통합고객번호
    SEQ            NUMBER        NOT NULL,  -- 순번
    PNT_TYPE_GBCD  VARCHAR2(2)   NOT NULL,  -- 포인트유형구분코드
    RGST_ID        VARCHAR2(100) NOT NULL,  -- 등록자ID
    RGST_IP        VARCHAR2(50)  NOT NULL,  -- 등록자IP
    REG_DTM        VARCHAR2(14)  NOT NULL,  -- 등록일시 (yyyyMMddHHmmss)
    CHGP_ID        VARCHAR2(100) NOT NULL,  -- 변경자ID
    CHGP_IP        VARCHAR2(30)  NOT NULL,  -- 변경자IP
    CHG_DTM        VARCHAR2(14)  NOT NULL,  -- 변경일시 (yyyyMMddHHmmss)
    CONSTRAINT PK_AP_CUST_PNT_PRTY_CHG_HIS PRIMARY KEY (MCUST_NO, SEQ)
);

COMMENT ON TABLE  AP_CUST_PNT_PRTY_CHG_HIS            IS '포인트 우선순위 변경 이력';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.MCUST_NO      IS '통합고객번호';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.SEQ           IS '순번';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.PNT_TYPE_GBCD IS '포인트유형구분코드';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.RGST_ID       IS '등록자ID';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.RGST_IP       IS '등록자IP';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.REG_DTM       IS '등록일시';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.CHGP_ID       IS '변경자ID';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.CHGP_IP       IS '변경자IP';
COMMENT ON COLUMN AP_CUST_PNT_PRTY_CHG_HIS.CHG_DTM       IS '변경일시';

-- SEQ 채번용 시퀀스
CREATE SEQUENCE AP_CUST_PNT_PRTY_CHG_HIS_SEQ
    START WITH 1
    INCREMENT BY 1
    NOCACHE;
```

---

## 조회 쿼리 예제

```sql
-- 전체 조회
SELECT * FROM AP_CUST_PNT_PRTY_CHG_HIS;

-- 특정 회원의 우선순위 변경 이력 (최신순)
SELECT * FROM AP_CUST_PNT_PRTY_CHG_HIS WHERE MCUST_NO = 'TEST0001' ORDER BY SEQ DESC;
```
