package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PEAAutoChargeVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String mcustNo;
    private String custKey;   // 토스 customerKey
    private String billKey;
    private String bankGbcd;  // 은행구분코드
}
