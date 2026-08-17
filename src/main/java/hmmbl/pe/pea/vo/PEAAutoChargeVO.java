package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "자동충전 결제계좌 등록 정보")
@Getter
@Setter
public class PEAAutoChargeVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    @Schema(description = "통합회원번호", example = "M0000000001")
    private String mcustNo;

    @Schema(description = "토스 customerKey", example = "customer_A1B2C3D4E5F6")
    private String custKey;

    @Schema(description = "토스 빌링키", hidden = true)
    private String billKey;

    @Schema(description = "은행구분코드", example = "004")
    private String bankGbcd;

    @Schema(description = "계좌순번", example = "1")
    private Integer acntSeq;

    @Schema(description = "계좌번호", hidden = true)
    private String acntNo;

    @Schema(description = "은행계좌이름", example = "카카오뱅크")
    private String bankAcntNm;

    @Schema(description = "등록자ID", example = "M0000000001")
    private String rgstId;

    @Schema(description = "등록자IP", example = "127.0.0.1")
    private String rgstIp;

    @Schema(description = "변경자ID", example = "M0000000001")
    private String chgpId;

    @Schema(description = "변경자IP", example = "127.0.0.1")
    private String chgpIp;
}
