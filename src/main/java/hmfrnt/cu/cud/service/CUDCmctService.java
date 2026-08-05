package hmfrnt.cu.cud.service;

import hmfrnt.common.HdgmMap;
import hmfrnt.cu.cud.vo.AncmVO;

/**
 * 고객센터 공지사항 서비스 인터페이스.
 */
public interface CUDCmctService {

    /** 공지사항 상세 조회 */
    AncmVO getAncmDtlView(HdgmMap paramMap) throws Exception;

    /** 공지사항 목록 조회 */
    HdgmMap getAncmDtlList(HdgmMap paramMap) throws Exception;
}
