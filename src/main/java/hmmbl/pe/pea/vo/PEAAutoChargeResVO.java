package hmmbl.pe.pea.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PEAAutoChargeResVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;

    @JsonIgnore  // billingKey 외부 응답 노출 금지
    private String billKey;

    private String paymentKey;
    private String orderId; // 토스 주문ID
    private String status;
    private String message;
}
