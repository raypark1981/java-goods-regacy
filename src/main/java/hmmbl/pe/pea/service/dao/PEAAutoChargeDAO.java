package hmmbl.pe.pea.service.dao;

import hmmbl.pe.pea.vo.PEAAutoChargeReqVO;
import hmmbl.pe.pea.vo.PEAAutoChargeVO;
import hmmbl.pe.pea.vo.PEAAutoChrgReqDtlVO;
import hmmbl.pe.pea.vo.PEAPntAutoChrgHisVO;
import hmmbl.pe.pea.vo.PEAPntInfVO;
import hmmbl.pe.pea.vo.PEAPntPrtyChgHisVO;
import hmmbl.pe.pea.vo.PEARsvChrgReqDtlVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PEAAutoChargeDAO {

    /** 충전 계좌(빌링키) 등록. */
    void insertChrgAcnt(PEAAutoChargeVO acntVO);

    /** 고객별 등록 계좌 목록 조회. */
    List<PEAAutoChargeVO> selectChrgAcntList(PEAAutoChargeReqVO reqVO);

    /** 우선순위 기준 상위 빌링키 단건 조회. */
    PEAAutoChargeVO selectBillingKey(String mcustNo);

    /** 고객 자동충전 신청 내역 등록. */
    void insertAutoChrgReqDtl(PEAAutoChrgReqDtlVO reqDtlVO);

    /** 고객포인트자동충전이력 등록. */
    void insertPntAutoChrgHis(PEAPntAutoChrgHisVO hisVO);

    /** 고객포인트현황 등록. */
    void insertPntInf(PEAPntInfVO pntInfVO);

    /** 포인트 우선순위 변경 이력 등록. */
    void insertPntPrtyChgHis(PEAPntPrtyChgHisVO prtyChgHisVO);

    /** 고객 예약 충전 신청 내역 등록. */
    void insertRsvChrgReqDtl(PEARsvChrgReqDtlVO rsvChrgReqDtlVO);

    /** 최대 충전 가능 금액 조회. TODO 하드코딩 -> 사용자 보유한도 조회 로직으로 교체 필요. */
    int selectMaxChargeAmt();

    /** mcustNo + paymentKey로 원본 충전 이력 조회. 취소 요청 시 소유권 확인 및 원본 정보(orderId, 충전금액) 재사용에 사용. */
    PEAPntAutoChrgHisVO selectPntAutoChrgHisByPaymentKey(@Param("mcustNo") String mcustNo, @Param("paymentKey") String paymentKey);
}