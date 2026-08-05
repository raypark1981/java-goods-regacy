package hmfrnt.ap.service.impl;

import hmfrnt.ap.mapper.APGoodsDAO;
import hmfrnt.ap.service.APGoodsService;
import hmfrnt.ap.vo.APGoodsVO;
import hmfrnt.common.HdgmAbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 상품 서비스 구현체.
 */
@Service
@Transactional(readOnly = true)
public class APGoodsServiceImpl extends HdgmAbstractService implements APGoodsService {

    private final APGoodsDAO apGoodsDAO;

    public APGoodsServiceImpl(APGoodsDAO apGoodsDAO) {
        this.apGoodsDAO = apGoodsDAO;
    }

    @Override
    public APGoodsVO getGoodsDetail(String gdCd) {
        return apGoodsDAO.selectGoodsDetail(gdCd);
    }

    @Override
    public List<APGoodsVO> getGoodsList() {
        return apGoodsDAO.selectGoodsList();
    }
}
