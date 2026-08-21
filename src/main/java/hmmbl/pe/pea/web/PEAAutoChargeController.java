package hmmbl.pe.pea.web;

import hmmbl.pe.pea.service.PEAAutoChargeService;
import hmmbl.pe.pea.vo.PEAAutoChargeReq;
import hmmbl.pe.pea.vo.PEAAutoChargeResVO;
import hmfrnt.web.ResultData;
import hmfrnt.web.BaseController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 자동충전 결제 Controller.
 */
@Tag(name = "자동충전 결제", description = "OTC 승인 서버 연동 자동충전 결제 API")
@Controller
@RequestMapping("/pe/pea")
public class PEAAutoChargeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PEAAutoChargeController.class);

    private final PEAAutoChargeService peaAutoChargeService;

    public PEAAutoChargeController(PEAAutoChargeService peaAutoChargeService) {
        this.peaAutoChargeService = peaAutoChargeService;
    }

    /** 빌링키 발급 및 저장. 토스 인증 완료 후 authKey + customerKey로 billingKey를 발급받아 DB에 저장한다. */
    @Operation(summary = "빌링키 발급", description = "토스 계좌 인증 완료 후 billingKey를 발급받아 저장한다.")
    @RequestMapping(value = "/billingAuthIssue.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> issueBillingKey(@RequestBody PEAAutoChargeReq.BillingAuthIssue reqVO, HttpServletRequest request) throws Exception {
        return ResultData.success(peaAutoChargeService.issueBillingKey(reqVO, request));
    }

    /** 자동충전 결제 요청. 토스 결제 실패 시 OTC 승인 서버에는 FAIL 응답으로 내려주고 종료한다. */
    @Operation(summary = "자동충전 결제", description = "등록된 빌링키로 토스 자동충전 결제를 요청한다.")
    @RequestMapping(value = "/autoCharge.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> requestAutoCharge(@RequestBody PEAAutoChargeReq.Charge reqVO) {
        try {
            return ResultData.success(peaAutoChargeService.requestAutoCharge(reqVO));
        } catch (Exception e) {
            logger.error("자동충전 결제 실패 - mcustNo: {}", reqVO.getMcustNo(), e);
            return ResultData.fail(e.getMessage());
        }
    }

    /** 자동충전 결제 취소 요청. 포인트 충전 실패 시 토스 결제 취소를 처리한다. */
    @Operation(summary = "자동충전 결제 취소", description = "포인트 충전 실패 등 보상 처리 시 토스 결제 취소를 요청한다.")
    @RequestMapping(value = "/autoChargeCancel.nhd", method = RequestMethod.POST)
    @ResponseBody
    public ResultData<PEAAutoChargeResVO> cancelAutoCharge(@RequestBody PEAAutoChargeReq.Cancel reqVO) {
        try {
            PEAAutoChargeResVO resVO = peaAutoChargeService.cancelAutoCharge(reqVO);
            return ResultData.success(resVO);
        } catch (Exception e) {
            logger.error("자동충전 결제 취소 실패 - mcustNo: {}", reqVO.getMcustNo(), e);
            return ResultData.fail(e.getMessage());
        }
    }

    /** 빌링 인증창 테스트 버튼 화면. */
    @RequestMapping(value = "/billingAuthPage.nhd", method = RequestMethod.GET)
    public String billingAuthPage() {
        return "pe/pea/billingAuth";
    }

    /** 빌링 인증 결과 수신 화면. successUrl로 돌아온 authKey를 그 자리에서 바로 빌링키 발급에 소진한다. */
    @RequestMapping(value = "/billingAuthResult.nhd", method = RequestMethod.GET)
    public String billingAuthResultPage(
            @RequestParam(required = false) String authKey,
            @RequestParam(required = false) String customerKey,
            @RequestParam(required = false) String message,
            Model model,
            HttpServletRequest request) {

        if (authKey == null || customerKey == null) {
            model.addAttribute("issueMessage", "인증 실패: " + message);
            return "pe/pea/billingAuthResult";
        }

        // TODO 나중에 세션정보(로그인 회원번호)에서 받아야 함. 현재는 테스트용 고정값.
        String mcustNo = "TEST0001";

        PEAAutoChargeReq.BillingAuthIssue reqVO = new PEAAutoChargeReq.BillingAuthIssue();
        reqVO.setAuthKey(authKey);
        reqVO.setCustomerKey(customerKey);
        reqVO.setMcustNo(mcustNo);
        // billingAuth.jsp가 토스 SDK를 method: "TRANSFER"로 고정 호출하고 있어 동일하게 맞춤.
        reqVO.setPayMethod("TRANSFER");

        try {
            PEAAutoChargeResVO resVO = peaAutoChargeService.issueBillingKey(reqVO, request);
            model.addAttribute("issueMessage", "빌링키 발급 성공 - mcustNo: " + resVO.getMcustNo());
            model.addAttribute("mcustNo", resVO.getMcustNo());
        } catch (Exception e) {
            model.addAttribute("issueMessage", "빌링키 발급 실패: " + e.getMessage());
        }

        return "pe/pea/billingAuthResult";
    }

    /** 자동충전 결제 테스트 화면. */
    @RequestMapping(value = "/autoChargeTest.nhd", method = RequestMethod.GET)
    public String autoChargeTestPage() {
        return "pe/pea/autoChargeTest";
    }
}
