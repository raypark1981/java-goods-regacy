# AP_CUST_PNT_INF (고객포인트현황)

> **테이블명 해석**: `AP`(승인) + `CUST`(고객) + `PNT`(포인트) + `INF`(정보/현황)  
> **기본 설명**: 승인 업무에서 고객별 포인트 보유 현황과 포인트 거래 누적 정보를 관리하는 테이블

> **구분**: 기존 테이블  
> **업무**: AP (승인) / PE (선불충전)  
> **용도**: 고객별 포인트 보유/적립/사용/소멸/교환 현황 관리

## 컬럼 정의

| 순번 | Key | 컬럼명 | 설명 | Type | Nullable | 비고 |
|------|-----|--------|------|------|----------|------|
| 1 | PK | MCUST_NO | 통합고객번호 | VARCHAR2(10) | N | |
| 2 | | AVLB_PNT | 가용포인트 | NUMBER | N | |
| 3 | | TSALE_ACMT_AMT | 총적립누적포인트 | NUMBER | Y | 사진 판독 기준 |
| 4 | | ACM_PNT | 적립포인트 | NUMBER | Y | |
| 5 | | ACM_CNT | 적립건수 | NUMBER | Y | |
| 6 | | ACM_CNCL_PNT | 적립취소포인트 | NUMBER | Y | |
| 7 | | ACM_CNCL_CNT | 적립취소건수 | NUMBER | Y | |
| 8 | | CAMP_PNT | 캠페인포인트 | NUMBER | Y | |
| 9 | | CAMP_CNT | 캠페인건수 | NUMBER | Y | |
| 10 | | CAMP_CNCL_PNT | 캠페인취소포인트 | NUMBER | Y | |
| 11 | | CAMP_CNCL_CNT | 캠페인취소건수 | NUMBER | Y | |
| 12 | | CLUB_ACM_PNT | 클럽적립포인트 | NUMBER | Y | |
| 13 | | CLUB_ACM_CNT | 클럽적립건수 | NUMBER | Y | |
| 14 | | CLUB_ACM_CNCL_PNT | 클럽적립취소포인트 | NUMBER | Y | |
| 15 | | CLUB_ACM_CNCL_CNT | 클럽적립취소건수 | NUMBER | Y | |
| 16 | | USE_PNT | 사용포인트 | NUMBER | Y | |
| 17 | | USE_CNT | 사용건수 | NUMBER | Y | |
| 18 | | USE_CNCL_PNT | 사용취소포인트 | NUMBER | Y | |
| 19 | | USE_CNCL_CNT | 사용취소건수 | NUMBER | Y | |
| 20 | | EXTN_PNT | 소멸포인트 | NUMBER | Y | |
| 21 | | EXTN_CNT | 소멸건수 | NUMBER | Y | |
| 22 | | TRF_PNT | 양도포인트 | NUMBER | Y | |
| 23 | | TRF_CNT | 양도건수 | NUMBER | Y | |
| 24 | | TAKE_PNT | 양수포인트 | NUMBER | Y | |
| 25 | | TAKE_CNT | 양수건수 | NUMBER | Y | |
| 26 | | TRF_STBY_PNT | 양도대기포인트 | NUMBER | Y | |
| 27 | | PNT_EXCH_RCV_PNT | 포인트교환수신포인트 | NUMBER | Y | |
| 28 | | PNT_EXCH_RCV_PNT_CNT | 포인트교환수신포인트건수 | NUMBER | Y | |
| 29 | | PNT_EXCH_SEND_PNT | 포인트교환전송포인트 | NUMBER | Y | |
| 30 | | PNT_EXCH_SEND_PNT_CNT | 포인트교환전송포인트건수 | NUMBER | Y | |
| 31 | | FRST_JOIN_DT | 최초가입일자 | VARCHAR2(8) | Y | |
| 32 | | FRST_APRVL_DT | 최초승인일자 | VARCHAR2(8) | Y | |
| 33 | | LAST_APRVL_DT | 최종승인일자 | VARCHAR2(8) | Y | |
| 34 | | FRST_APRVL_PTCO_ID | 최초승인참여사ID | VARCHAR2(4) | Y | |
| 35 | | FRST_APRVL_FRCS_ID | 최초승인가맹점ID | VARCHAR2(5) | Y | |
| 36 | | LAST_APRVL_PTCO_ID | 최종승인참여사ID | VARCHAR2(4) | Y | |
| 37 | | LAST_APRVL_FRCS_ID | 최종승인가맹점ID | VARCHAR2(5) | Y | |
| 38 | | LAST_APLEDG_PTCNO | 최종승인원장상세번호 | VARCHAR2(19) | Y | |
| 39 | | MOPH_OCCP_CERT_NO | 휴대전화점유인증번호 | VARCHAR2(10) | Y | |
| 40 | | MOPH_OCCP_CERT_NO_SEND_DTM | 휴대전화점유인증번호전송일시 | VARCHAR2(14) | Y | |
| 41 | | APP_CRD_FRST_ACM_DT | 애플리케이션카드최초적립일자 | VARCHAR2(8) | Y | |
| 42 | | APP_FRST_APRVL_LEDG_NO | 애플리케이션최초승인원장번호 | VARCHAR2(18) | Y | |
| 43 | | RGST_ID | 등록자ID | VARCHAR2(100) | N | |
| 44 | | RGST_IP | 등록자IP | VARCHAR2(50) | N | |
| 45 | | REG_DTM | 등록일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 46 | | CHGP_ID | 변경자ID | VARCHAR2(100) | N | |
| 47 | | CHGP_IP | 변경자IP | VARCHAR2(50) | N | |
| 48 | | CHG_DTM | 변경일시 | VARCHAR2(14) | N | yyyyMMddHHmmss |
| 49 | | PNT_TYPE_GBCD | 포인트유형구분코드 | VARCHAR2(2) | Y | |

> 기존 초안의 `SCDR_CERT_IN`, `SCDR_CERT_DT`는 첨부 이미지에서 확인되지 않아 생성 쿼리에서는 제외했다.

---

## CREATE TABLE

```sql
CREATE TABLE AP_CUST_PNT_INF (
    MCUST_NO                    VARCHAR2(10)  NOT NULL,  -- 통합고객번호
    AVLB_PNT                    NUMBER        NOT NULL,  -- 가용포인트
    TSALE_ACMT_AMT               NUMBER        NULL,      -- 총적립누적포인트
    ACM_PNT                     NUMBER        NULL,      -- 적립포인트
    ACM_CNT                     NUMBER        NULL,      -- 적립건수
    ACM_CNCL_PNT                NUMBER        NULL,      -- 적립취소포인트
    ACM_CNCL_CNT                NUMBER        NULL,      -- 적립취소건수
    CAMP_PNT                    NUMBER        NULL,      -- 캠페인포인트
    CAMP_CNT                    NUMBER        NULL,      -- 캠페인건수
    CAMP_CNCL_PNT               NUMBER        NULL,      -- 캠페인취소포인트
    CAMP_CNCL_CNT               NUMBER        NULL,      -- 캠페인취소건수
    CLUB_ACM_PNT                NUMBER        NULL,      -- 클럽적립포인트
    CLUB_ACM_CNT                NUMBER        NULL,      -- 클럽적립건수
    CLUB_ACM_CNCL_PNT           NUMBER        NULL,      -- 클럽적립취소포인트
    CLUB_ACM_CNCL_CNT           NUMBER        NULL,      -- 클럽적립취소건수
    USE_PNT                     NUMBER        NULL,      -- 사용포인트
    USE_CNT                     NUMBER        NULL,      -- 사용건수
    USE_CNCL_PNT                NUMBER        NULL,      -- 사용취소포인트
    USE_CNCL_CNT                NUMBER        NULL,      -- 사용취소건수
    EXTN_PNT                    NUMBER        NULL,      -- 소멸포인트
    EXTN_CNT                    NUMBER        NULL,      -- 소멸건수
    TRF_PNT                     NUMBER        NULL,      -- 양도포인트
    TRF_CNT                     NUMBER        NULL,      -- 양도건수
    TAKE_PNT                    NUMBER        NULL,      -- 양수포인트
    TAKE_CNT                    NUMBER        NULL,      -- 양수건수
    TRF_STBY_PNT                NUMBER        NULL,      -- 양도대기포인트
    PNT_EXCH_RCV_PNT            NUMBER        NULL,      -- 포인트교환수신포인트
    PNT_EXCH_RCV_PNT_CNT        NUMBER        NULL,      -- 포인트교환수신포인트건수
    PNT_EXCH_SEND_PNT           NUMBER        NULL,      -- 포인트교환전송포인트
    PNT_EXCH_SEND_PNT_CNT       NUMBER        NULL,      -- 포인트교환전송포인트건수
    FRST_JOIN_DT                VARCHAR2(8)   NULL,      -- 최초가입일자
    FRST_APRVL_DT               VARCHAR2(8)   NULL,      -- 최초승인일자
    LAST_APRVL_DT               VARCHAR2(8)   NULL,      -- 최종승인일자
    FRST_APRVL_PTCO_ID          VARCHAR2(4)   NULL,      -- 최초승인참여사ID
    FRST_APRVL_FRCS_ID          VARCHAR2(5)   NULL,      -- 최초승인가맹점ID
    LAST_APRVL_PTCO_ID          VARCHAR2(4)   NULL,      -- 최종승인참여사ID
    LAST_APRVL_FRCS_ID          VARCHAR2(5)   NULL,      -- 최종승인가맹점ID
    LAST_APLEDG_PTCNO           VARCHAR2(19)  NULL,      -- 최종승인원장상세번호
    MOPH_OCCP_CERT_NO           VARCHAR2(10)  NULL,      -- 휴대전화점유인증번호
    MOPH_OCCP_CERT_NO_SEND_DTM  VARCHAR2(14)  NULL,      -- 휴대전화점유인증번호전송일시
    APP_CRD_FRST_ACM_DT         VARCHAR2(8)   NULL,      -- 애플리케이션카드최초적립일자
    APP_FRST_APRVL_LEDG_NO      VARCHAR2(18)  NULL,      -- 애플리케이션최초승인원장번호
    RGST_ID                     VARCHAR2(100) NOT NULL,  -- 등록자ID
    RGST_IP                     VARCHAR2(50)  NOT NULL,  -- 등록자IP
    REG_DTM                     VARCHAR2(14)  NOT NULL,  -- 등록일시 (yyyyMMddHHmmss)
    CHGP_ID                     VARCHAR2(100) NOT NULL,  -- 변경자ID
    CHGP_IP                     VARCHAR2(50)  NOT NULL,  -- 변경자IP
    CHG_DTM                     VARCHAR2(14)  NOT NULL,  -- 변경일시 (yyyyMMddHHmmss)
    PNT_TYPE_GBCD               VARCHAR2(2)   NULL,      -- 포인트유형구분코드
    CONSTRAINT PK_AP_CUST_PNT_INF PRIMARY KEY (MCUST_NO)
);

COMMENT ON TABLE  AP_CUST_PNT_INF                              IS '고객포인트현황';
COMMENT ON COLUMN AP_CUST_PNT_INF.MCUST_NO                     IS '통합고객번호';
COMMENT ON COLUMN AP_CUST_PNT_INF.AVLB_PNT                     IS '가용포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.TSALE_ACMT_AMT                IS '총적립누적포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.ACM_PNT                      IS '적립포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.ACM_CNT                      IS '적립건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.ACM_CNCL_PNT                 IS '적립취소포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.ACM_CNCL_CNT                 IS '적립취소건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.CAMP_PNT                     IS '캠페인포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.CAMP_CNT                     IS '캠페인건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.CAMP_CNCL_PNT                IS '캠페인취소포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.CAMP_CNCL_CNT                IS '캠페인취소건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.CLUB_ACM_PNT                 IS '클럽적립포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.CLUB_ACM_CNT                 IS '클럽적립건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.CLUB_ACM_CNCL_PNT            IS '클럽적립취소포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.CLUB_ACM_CNCL_CNT            IS '클럽적립취소건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.USE_PNT                      IS '사용포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.USE_CNT                      IS '사용건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.USE_CNCL_PNT                 IS '사용취소포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.USE_CNCL_CNT                 IS '사용취소건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.EXTN_PNT                     IS '소멸포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.EXTN_CNT                     IS '소멸건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.TRF_PNT                      IS '양도포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.TRF_CNT                      IS '양도건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.TAKE_PNT                     IS '양수포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.TAKE_CNT                     IS '양수건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.TRF_STBY_PNT                 IS '양도대기포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_EXCH_RCV_PNT             IS '포인트교환수신포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_EXCH_RCV_PNT_CNT         IS '포인트교환수신포인트건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_EXCH_SEND_PNT            IS '포인트교환전송포인트';
COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_EXCH_SEND_PNT_CNT        IS '포인트교환전송포인트건수';
COMMENT ON COLUMN AP_CUST_PNT_INF.FRST_JOIN_DT                 IS '최초가입일자';
COMMENT ON COLUMN AP_CUST_PNT_INF.FRST_APRVL_DT                IS '최초승인일자';
COMMENT ON COLUMN AP_CUST_PNT_INF.LAST_APRVL_DT                IS '최종승인일자';
COMMENT ON COLUMN AP_CUST_PNT_INF.FRST_APRVL_PTCO_ID           IS '최초승인참여사ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.FRST_APRVL_FRCS_ID           IS '최초승인가맹점ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.LAST_APRVL_PTCO_ID           IS '최종승인참여사ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.LAST_APRVL_FRCS_ID           IS '최종승인가맹점ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.LAST_APLEDG_PTCNO            IS '최종승인원장상세번호';
COMMENT ON COLUMN AP_CUST_PNT_INF.MOPH_OCCP_CERT_NO            IS '휴대전화점유인증번호';
COMMENT ON COLUMN AP_CUST_PNT_INF.MOPH_OCCP_CERT_NO_SEND_DTM   IS '휴대전화점유인증번호전송일시';
COMMENT ON COLUMN AP_CUST_PNT_INF.APP_CRD_FRST_ACM_DT          IS '애플리케이션카드최초적립일자';
COMMENT ON COLUMN AP_CUST_PNT_INF.APP_FRST_APRVL_LEDG_NO       IS '애플리케이션최초승인원장번호';
COMMENT ON COLUMN AP_CUST_PNT_INF.RGST_ID                      IS '등록자ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.RGST_IP                      IS '등록자IP';
COMMENT ON COLUMN AP_CUST_PNT_INF.REG_DTM                      IS '등록일시';
COMMENT ON COLUMN AP_CUST_PNT_INF.CHGP_ID                      IS '변경자ID';
COMMENT ON COLUMN AP_CUST_PNT_INF.CHGP_IP                      IS '변경자IP';
COMMENT ON COLUMN AP_CUST_PNT_INF.CHG_DTM                      IS '변경일시';
COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_TYPE_GBCD                IS '포인트유형구분코드';
```

## 신규 컬럼 추가 쿼리

이미 운영 테이블이 존재하고 `PNT_TYPE_GBCD`만 추가하는 경우 아래 쿼리를 사용한다.

```sql
ALTER TABLE AP_CUST_PNT_INF ADD (
    PNT_TYPE_GBCD VARCHAR2(2) NULL  -- 포인트유형구분코드
);

COMMENT ON COLUMN AP_CUST_PNT_INF.PNT_TYPE_GBCD IS '포인트유형구분코드';
```

---

## 조회 쿼리 예제

```sql
-- 전체 조회
SELECT * FROM AP_CUST_PNT_INF;

-- 특정 회원 포인트 현황 조회
SELECT * FROM AP_CUST_PNT_INF WHERE MCUST_NO = 'TEST0001';
```
