package hmfrnt.ap.vo;

import hmfrnt.web.BaseObject;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 상품 조회 VO (DB 결과 매핑).
 */
@Getter
@Setter
@ToString
public class APGoodsVO extends BaseObject {

    private static final long serialVersionUID = 1L;

    private String gdCd;     // 상품코드 (DB: GD_CD)
    private String gdNm;     // 상품명   (DB: GD_NM)
    private int    gdPrice;  // 판매가   (DB: GD_PRICE)
    private String gdDesc;   // 상품설명 (DB: GD_DESC)
    private String gdImgUrl; // 이미지   (DB: GD_IMG_URL)
    private String useYn;    // 사용여부 (DB: USE_YN)
}
