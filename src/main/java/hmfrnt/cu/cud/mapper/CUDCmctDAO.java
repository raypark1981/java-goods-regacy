package hmfrnt.cu.cud.mapper;

import hmfrnt.common.HdgmMap;
import hmfrnt.cu.cud.vo.AncmVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 고객센터 공지사항 DAO (MyBatis Mapper Interface).
 * SQL은 resources/mapper/cu/cud/CUDCmctMapper.xml 에 작성한다.
 */
@Mapper
public interface CUDCmctDAO {

    /** 공지사항 상세 조회 */
    AncmVO selectAncmDtlView(HdgmMap paramMap) throws Exception;

    /** 공지사항 목록 조회 */
    HdgmMap selectAncmDtlList(HdgmMap paramMap) throws Exception;
}
