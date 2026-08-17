package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PEAAutoChargeReqVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;       // 통합고객번호
    private int chargeAmt;        // 충전금액
    private String orderId;       // 토스 주문ID
    private String posNo;         // POS번호
    private String orderName;     // 주문명₩
    private String customerEmail; // 고객 이메일
    private String customerName;  // 고객명
    private String authKey;       // 토스 빌링 인증키 (successUrl 수신값)
    private String customerKey;   // 토스 고객 식별값
    private String paymentKey;    // 취소할 토스 결제키
    private String cancelReason;  // 취소 사유

}
