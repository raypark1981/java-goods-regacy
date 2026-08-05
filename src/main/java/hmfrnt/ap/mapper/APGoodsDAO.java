package hmfrnt.ap.mapper;

import hmfrnt.ap.vo.APGoodsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 상품 DAO (MyBatis Mapper Interface).
 * SQL은 resources/mapper/ap/APGoodsMapper.xml 에 작성한다.
 */
@Mapper
public interface APGoodsDAO {

    APGoodsVO selectGoodsDetail(@Param("gdCd") String gdCd);

    List<APGoodsVO> selectGoodsList();
}
