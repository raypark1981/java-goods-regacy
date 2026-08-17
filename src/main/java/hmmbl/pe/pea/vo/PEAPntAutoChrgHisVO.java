package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * AP_CUST_PNT_AUTO_CHRG_HIS (고객포인트자동충전이력) VO.
 */
@Getter
@Setter
public class PEAPntAutoChrgHisVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;            // 고객번호
    private String paymentKey;         // 결제키 (토스 paymentKey)
    private String orderId;            // 주문ID (토스 orderId)
    private String chrgTypeGbcd;       // 충전유형구분코드
    private String rsvChrgMthdGbcd;    // 예약충전방법구분코드
    private String rsvChrgPrdGbcd;     // 예약충전주기구분코드
    private String rsvChrgDtGbcd;      // 예약충전일구분코드
    private String rsvBsicAmtGbcd;     // 예약기준금액구분코드
    private Integer acntChrgAmt;       // 계좌충전금액
    private Integer realChrgAmt;       // 실충전금액
    private Integer acntSeq;           // 계좌순번
    private String bankGbcd;           // 은행구분코드
    private String acntNo;             // 계좌번호
    private String procRstGbcd;        // 처리결과구분코드
    private String errCd;              // 오류코드
    private String errMsgCntn;         // 오류메시지내용
}
