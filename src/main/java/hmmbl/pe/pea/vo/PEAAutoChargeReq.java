package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 자동충전 결제 API 요청 DTO 모음. 엔드포인트별로 실제 쓰는 필드만 나눠서 Swagger 문서에
 * 불필요한 필드가 노출되지 않게 한다.
 */
public class PEAAutoChargeReq {

    @Getter
    @Setter
    public static class BillingAuthIssue extends BaseVo {
        private static final long serialVersionUID = 1L;

        private String mcustNo;     // 통합고객번호
        private String authKey;     // 토스 빌링 인증키 (successUrl 수신값)
        private String customerKey; // 토스 고객 식별값

        @Schema(example = "TRANSFER")
        private String payMethod;   // 결제수단 (PayMethod 코드값)
    }

    @Getter
    @Setter
    public static class Charge extends BaseVo {
        private static final long serialVersionUID = 1L;

        @Schema(example = "TEST0001")
        private String mcustNo;       // 통합고객번호

        @Schema(example = "30000")
        private int chargeAmt;        // 충전금액

        @Schema(example = "", description = "미입력 시 서버가 자동 생성")
        private String orderId;       // 토스 주문ID

        @Schema(example = "자동충전 테스트")
        private String orderName;     // 주문명

        @Schema(example = "test@example.com")
        private String customerEmail; // 고객 이메일

        @Schema(example = "테스트")
        private String customerName;  // 고객명

        @Schema(example = "TRANSFER")
        private String payMethod;     // 결제수단 (PayMethod 코드값)
    }

    @Getter
    @Setter
    public static class Cancel extends BaseVo {
        private static final long serialVersionUID = 1L;

        private String mcustNo;      // 통합고객번호
        private String paymentKey;   // 취소할 토스 결제키
        private String cancelReason; // 취소 사유

        @Schema(example = "TRANSFER")
        private String payMethod;    // 결제수단 (PayMethod 코드값)
    }
}
