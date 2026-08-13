package hmmbl.pe.pea.service;

import hmmbl.pe.pea.vo.PEAAutoChargeReqVO;
import hmmbl.pe.pea.vo.PEAAutoChargeResVO;

public interface PEAAutoChargeService {

    /** 빌링키 발급 및 저장. 토스 인증 완료 후 authKey + customerKey로 billingKey를 발급받아 DB에 저장한다. */
    PEAAutoChargeResVO issueBillingKey(PEAAutoChargeReqVO reqVO) throws Exception;

    /** 자동충전 결제 요청. 빌링키 조회 → 금액 산정 → 토스 결제 → NICE 포인트 충전 순으로 처리한다. */
    PEAAutoChargeResVO requestAutoCharge(PEAAutoChargeReqVO reqVO) throws Exception;

    /** 자동충전 결제 취소 요청. NICE 포인트 충전 실패 시 토스 결제를 취소한다. */
    PEAAutoChargeResVO cancelAutoCharge(PEAAutoChargeReqVO reqVO) throws Exception;
}
