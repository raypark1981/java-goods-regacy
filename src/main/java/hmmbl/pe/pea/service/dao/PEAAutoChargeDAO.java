package hmmbl.pe.pea.service.dao;

import hmmbl.pe.pea.vo.PEAAutoChargeReqVO;
import hmmbl.pe.pea.vo.PEAAutoChargeVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PEAAutoChargeDAO {

    /** 충전 계좌(빌링키) 등록. */
    void insertChrgAcnt(PEAAutoChargeVO acntVO);

    /** 고객별 등록 계좌 목록 조회. */
    List<PEAAutoChargeVO> selectChrgAcntList(PEAAutoChargeReqVO reqVO);

    /** 우선순위 기준 상위 빌링키 단건 조회. */
    PEAAutoChargeVO selectBillingKey(String mcustNo);
}