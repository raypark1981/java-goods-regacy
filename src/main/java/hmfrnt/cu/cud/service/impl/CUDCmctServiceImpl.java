package hmfrnt.cu.cud.service.impl;

import hmfrnt.common.HdgmAbstractService;
import hmfrnt.common.HdgmMap;
import hmfrnt.cu.cud.mapper.CUDCmctDAO;
import hmfrnt.cu.cud.service.CUDCmctService;
import hmfrnt.cu.cud.vo.AncmVO;
import org.springframework.stereotype.Service;

/**
 * 고객센터 공지사항 서비스 구현체.
 * HdgmAbstractService 상속 + CUDCmctService 구현.
 */
@Service
public class CUDCmctServiceImpl extends HdgmAbstractService implements CUDCmctService {

    private final CUDCmctDAO cudCmctDAO;

    public CUDCmctServiceImpl(CUDCmctDAO cudCmctDAO) {
        this.cudCmctDAO = cudCmctDAO;
    }

    @Override
    public AncmVO getAncmDtlView(HdgmMap paramMap) throws Exception {
        return cudCmctDAO.selectAncmDtlView(paramMap);
    }

    @Override
    public HdgmMap getAncmDtlList(HdgmMap paramMap) throws Exception {
        return cudCmctDAO.selectAncmDtlList(paramMap);
    }
}
