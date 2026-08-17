package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * AP_CUST_RSV_CHRG_REQ_DTL (고객 예약 충전 신청 내역) VO.
 */
@Getter
@Setter
public class PEARsvChrgReqDtlVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;            // 통합고객번호
    private Integer pntAcntGcd;        // 선불계좌구분코드
    private Integer pntAcntSno;        // 선불계좌순번
    private String rsvChrgMthdGbcd;    // 예약충전방법구분코드
    private String rsvChrgPrdGcd;      // 예약충전기간구분코드
    private String rsvOrgDujGbcd;      // 예약충전기준구분코드
    private String rsvChrgDuj;         // 예약충전일
    private String rsvBscAmtGbcd;      // 예약기준금액구분코드
    private Integer rsvBscAmt;         // 예약기준금액
    private Integer acntChrgAmt;       // 계좌충전금액
    private String dslYn;              // 삭제여부 (Y/N)
    private String delDtm;             // 삭제일시
    private String rgstId;             // 등록자ID
    private String rgstIp;             // 등록자IP
    private String chgpId;             // 변경자ID
    private String chgpIp;             // 변경자IP
}
