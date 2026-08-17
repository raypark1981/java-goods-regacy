package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

/**
 * AP_CUST_AUTO_CHRG_REQ_DTL (고객 자동충전 신청 내역) VO.
 */
@Getter
@Setter
public class PEAAutoChrgReqDtlVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;       // 통합고객번호
    private String autoChrgYn;    // 자동충전여부 (Y/N)
    private Integer pty1AcntSeq;  // 1순위계좌순번
    private Integer pty2AcntSeq;  // 2순위계좌순번
    private String rgstId;        // 등록자ID
    private String rgstIp;        // 등록자IP
    private String chgpId;        // 변경자ID
    private String chgpIp;        // 변경자IP
}
