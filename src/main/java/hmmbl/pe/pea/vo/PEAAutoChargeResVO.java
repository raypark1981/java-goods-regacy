package hmmbl.pe.pea.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hmfrnt.web.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "포인트 자동충전 결제 응답")
@Getter
@Setter
public class PEAAutoChargeResVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "통합회원번호", example = "M0000000001")
    private String mcustNo;

    @JsonIgnore  // billingKey 외부 응답 노출 금지
    @Schema(description = "빌링키 (외부 응답 미노출)", hidden = true)
    private String billKey;

    @Schema(description = "토스페이먼츠 결제키", example = "5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6")
    private String paymentKey;

    @Schema(description = "토스 주문ID", example = "a4CWyWY5m89PNh7xJwhk1")
    private String orderId;

    @Schema(description = "결제 처리 상태 (예: SUCCESS, FAIL)", example = "SUCCESS")
    private String status;

    @Schema(description = "결제 처리 결과 메시지")
    private String message;
}
