package com.hyundaicard.mmall.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GoodsVO {

    private String gdCd;       // 상품코드 (DB: GD_CD)
    private String gdNm;       // 상품명   (DB: GD_NM)
    private int    gdPrice;    // 판매가   (DB: GD_PRICE)
    private String gdDesc;     // 상품설명 (DB: GD_DESC)
    private String gdImgUrl;   // 이미지   (DB: GD_IMG_URL)
    private String useYn;      // 사용여부 (DB: USE_YN)

}
