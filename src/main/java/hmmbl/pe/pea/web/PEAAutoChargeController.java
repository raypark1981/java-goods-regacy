package hmmbl.pe.pea.web;

import hmmbl.pe.pea.service.PEAAutoChargeService;
import hmmbl.pe.pea.vo.PEAAutoChargeReqVO;
import hmmbl.pe.pea.vo.PEAAutoChargeResVO;
import hmfrnt.web.ResultData;
import hmfrnt.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 자동충전 결제 Controller.
 */
@Tag(name = "자동충전 결제", description = "OTC 승인 서버 연동 자동충전 결제 API")
@Controller
@RequestMapping("/pe/pea")
public class PEAAutoChargeController extends BaseController {

    private final PEAAutoChargeService peaAutoChargeService;

    public PEAAutoChargeController(PEAAutoChargeService peaAutoChargeService) {
        this.peaAutoChargeService = peaAutoChargeService;
    }

    /** 빌링키 발급 및 저장. 토스 인증 완료 후 authKey + customerKey로 billingKey를 발급받아 DB에 저장한다. */
    @Operation(summary = "빌링키 발급", description = "토스 계좌 인증 완료 후 billingKey를 발급받아 저장한다.")
    @RequestMapping(value = "/billingAuthIssue.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> issueBillingKey(@RequestBody PEAAutoChargeReqVO reqVO) throws Exception {
        return ResultData.success(peaAutoChargeService.issueBillingKey(reqVO));
    }

    /** 자동충전 결제 요청. */
    @Operation(summary = "자동충전 결제", description = "등록된 빌링키로 토스 자동충전 결제를 요청한다.")
    @RequestMapping(value = "/autoCharge.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> requestAutoCharge(@RequestBody PEAAutoChargeReqVO reqVO) throws Exception {
        return ResultData.success(peaAutoChargeService.requestAutoCharge(reqVO));
    }

    /** 자동충전 결제 취소 요청. 포인트 충전 실패 시 토스 결제 취소를 처리한다. */
    @Operation(summary = "자동충전 결제 취소", description = "포인트 충전 실패 등 보상 처리 시 토스 결제 취소를 요청한다.")
    @RequestMapping(value = "/autoChargeCancel.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> cancelAutoCharge(@RequestBody PEAAutoChargeReqVO reqVO) throws Exception {
        return ResultData.success(peaAutoChargeService.cancelAutoCharge(reqVO));
    }
}
