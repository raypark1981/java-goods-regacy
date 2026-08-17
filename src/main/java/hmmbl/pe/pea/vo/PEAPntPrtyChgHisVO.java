package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * AP_CUST_PNT_PRTY_CHG_HIS (포인트 우선순위 변경 이력) VO.
 */
@Getter
@Setter
public class PEAPntPrtyChgHisVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;      // 통합고객번호
    private String pntTypeGbcd;  // 포인트유형구분코드
    private String rgstId;       // 등록자ID
    private String rgstIp;       // 등록자IP
    private String chgpId;       // 변경자ID
    private String chgpIp;       // 변경자IP
}
