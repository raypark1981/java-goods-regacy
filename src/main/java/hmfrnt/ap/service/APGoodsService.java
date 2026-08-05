package hmfrnt.ap.service;

import hmfrnt.ap.vo.APGoodsVO;

import java.util.List;

/**
 * 상품 서비스 인터페이스.
 */
public interface APGoodsService {

    APGoodsVO getGoodsDetail(String gdCd);

    List<APGoodsVO> getGoodsList();
}
