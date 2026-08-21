package hmmbl.pe.pea.service.impl;

import hmmbl.pe.pea.PayMethod;
import hmmbl.pe.pea.exception.AutoChargeException;
import hmmbl.pe.pea.service.PEAAutoChargeService;
import hmmbl.pe.pea.service.dao.PEAAutoChargeDAO;
import hmmbl.pe.pea.util.PEAAutoChargeUtil;
import hmmbl.pe.pea.vo.PEAAutoChargeReq;
import hmmbl.pe.pea.vo.PEAAutoChargeResVO;
import hmmbl.pe.pea.vo.PEAAutoChargeVO;
import hmmbl.pe.pea.vo.PEAPntAutoChrgHisVO;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class PEAAutoChargeServiceImpl implements PEAAutoChargeService {

    private static final Logger logger = LoggerFactory.getLogger(PEAAutoChargeServiceImpl.class);
    private static final String TOSS_BILLING_AUTH_URL    = "https://api.tosspayments.com/v1/billing/authorizations/issue";
    private static final String TOSS_BILLING_PAYMENT_URL = "https://api.tosspayments.com/v1/billing/";
    private static final String TOSS_PAYMENT_URL     = "https://api.tosspayments.com/v1/payments/";
    private static final String TOSS_SECRET_KEY_ENV = "TOSS_SECRET_KEY";
    //TOSS_SECRET_KEY=test_sk_eqRGgYO1r5jywQJZqn518QnN2Eya
//    TOSS_SECRET_KEY=test_sk_DpexMgkW36oknkdeNL6BVGbR5ozO

    // 문서(AP_CUST_PNT_AUTO_CHRG_HIS.md) 참조
    private static final String CHRG_TYPE_AUTO    = "01"; // 충전유형구분코드: 자동
    // 임시 확인
    private static final String RSV_GBCD_NA       = "00"; // 예약충전 전용 구분코드: 자동충전 건은 해당없음
    // 문서(공통코드.md, PROC_RST_GBCD) 참조
    private static final String PROC_RST_SUCCESS  = "01"; // 처리결과구분코드: 성공
    // 문서(공통코드.md, PROC_RST_GBCD) 참조
    private static final String PROC_RST_FAIL     = "02"; // 처리결과구분코드: 실패
    // 임시 확인 (취소 코드값은 위키 캡처에 없었음, 성공/실패만 확인됨)
    private static final String PROC_RST_CANCEL   = "03"; // 처리결과구분코드: 취소

    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    private final PEAAutoChargeDAO peaAutoChargeDAO;

    public PEAAutoChargeServiceImpl(PEAAutoChargeDAO peaAutoChargeDAO) {
        this.peaAutoChargeDAO = peaAutoChargeDAO;
    }

    /** 빌링키 발급 및 저장. */
    @Override
    public PEAAutoChargeResVO issueBillingKey(PEAAutoChargeReq.BillingAuthIssue reqVO, HttpServletRequest request) throws Exception {
        logger.debug("issueBillingKey start");

        PayMethod payMethod = PayMethod.of(reqVO.getPayMethod());
        logger.debug("issueBillingKey payMethod: {}", payMethod);

        String secretKey = System.getenv(TOSS_SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("토스 시크릿키 환경변수(" + TOSS_SECRET_KEY_ENV + ")가 설정되지 않았습니다.");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("authKey",     reqVO.getAuthKey());
        requestBody.put("customerKey", reqVO.getCustomerKey());

        JSONObject tossRes = postJson(TOSS_BILLING_AUTH_URL, secretKey, requestBody);

        int statusCode = (Integer) tossRes.get("statusCode");
        if (statusCode < 200 || statusCode >= 300) {
            logger.error("토스 빌링키 발급 실패 - status: {}, body: {}", statusCode, tossRes);
            throw new IllegalStateException("토스 빌링키 발급에 실패했습니다.");
        }

        String billingKey = (String) tossRes.get("billingKey");
        logger.info("토스 빌링키 발급 완료 - mcustNo: {}", reqVO.getMcustNo());

        // transfers[0] - 계좌이체 빌링키 발급 응답에만 포함됨 (bankCode, bankName, bankAccountNumber(마스킹))
        JSONArray transfers = (JSONArray) tossRes.get("transfers");
        if (transfers == null || transfers.isEmpty()) {
            logger.error("토스 응답에 계좌 정보(transfers)가 없습니다 - mcustNo: {}", reqVO.getMcustNo());
            throw new IllegalStateException("토스 응답에 계좌 정보가 없습니다.");
        }
        JSONObject transfer = (JSONObject) transfers.get(0);
        String bankGbcd = (String) transfer.get("bankCode");
        String bankAcntNm = (String) transfer.get("bankName");
        String acntNo = (String) transfer.get("bankAccountNumber");

        Map<String, Object> maxAcntSeqMap = peaAutoChargeDAO.selectMaxAcntSeq(reqVO.getMcustNo());
        Number maxAcntSeq = (Number) maxAcntSeqMap.get("ACNT_SEQ");
        int acntSeq = maxAcntSeq == null ? 1 : maxAcntSeq.intValue() + 1;

        Map<String, Object> acntMap = new HashMap<>();
        acntMap.put("mcustNo", reqVO.getMcustNo());
        acntMap.put("custKey", reqVO.getCustomerKey());
        acntMap.put("billKey", billingKey);
        acntMap.put("bankGbcd", bankGbcd);
        acntMap.put("bankAcntNm", bankAcntNm);
        acntMap.put("acntNo", acntNo);
        acntMap.put("acntSeq", acntSeq);
        // 임시 확인 TODO 세션 로그인 정보(등록자ID)로 교체 필요. 현재는 임시값.
        acntMap.put("rgstId", reqVO.getMcustNo());
        acntMap.put("rgstIp", request.getRemoteAddr());
        acntMap.put("chgpId", reqVO.getMcustNo());
        acntMap.put("chgpIp", request.getRemoteAddr());
        peaAutoChargeDAO.insertChrgAcnt(acntMap);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo(reqVO.getMcustNo());

        logger.debug("issueBillingKey end");
        return resVO;
    }

    /** 자동충전 결제 요청. */
    @Override
    public PEAAutoChargeResVO requestAutoCharge(PEAAutoChargeReq.Charge reqVO) throws Exception {
        logger.debug("requestAutoCharge start");

        PayMethod payMethod = PayMethod.of(reqVO.getPayMethod());
        logger.debug("requestAutoCharge payMethod: {}", payMethod);

        Map<String, Object> billingInfo = validationBillingKey(reqVO.getMcustNo());

        int maxChargeAmt = peaAutoChargeDAO.selectMaxChargeAmt();
        int chargeAmt = PEAAutoChargeUtil.calculateChargeAmount(reqVO.getChargeAmt(), maxChargeAmt);
        logger.info("자동충전 금액 산정 완료 - mcustNo: {}, 요청금액: {}, 충전금액: {}",
                reqVO.getMcustNo(), reqVO.getChargeAmt(), chargeAmt);

        Integer acntSeq = (Integer) billingInfo.get("acntSeq");
        String bankGbcd = (String) billingInfo.get("bankGbcd");
        String acntNo = (String) billingInfo.get("acntNo");
        String bankAcntNm = (String) billingInfo.get("bankAcntNm");

        Map<String, Object> hisParams = new HashMap<>();
        hisParams.put("mcustNo", reqVO.getMcustNo());
        hisParams.put("chargeAmt", chargeAmt);
        hisParams.put("acntSeq", acntSeq);
        hisParams.put("bankGbcd", bankGbcd);
        hisParams.put("acntNo", acntNo);
        hisParams.put("bankAcntNm", bankAcntNm);

        JSONObject tossPayment;
        try {
            tossPayment = requestTossBillingPayment(billingInfo, reqVO, chargeAmt);
        } catch (Exception e) {
            hisParams.put("realChrgAmt", 0);
            hisParams.put("procRstGbcd", PROC_RST_FAIL);
            hisParams.put("errMsgCntn", e.getMessage());
            insertAutoChrgHis(hisParams);
            throw e;
        }

        String paymentKey = (String) tossPayment.get("paymentKey");
        String orderId = (String) tossPayment.get("orderId");
        Number totalAmount = (Number) tossPayment.get("totalAmount");
        int realChrgAmt = totalAmount == null ? chargeAmt : totalAmount.intValue();
        hisParams.put("paymentKey", paymentKey);
        hisParams.put("orderId", orderId);
        hisParams.put("realChrgAmt", realChrgAmt);

        boolean niceSuccess = requestNiceCharge(reqVO.getMcustNo(), realChrgAmt);
        if (!niceSuccess) {
            String errMsg = "NICE 포인트 충전 실패";
            try {
                requestTossPaymentCancel(paymentKey, "NICE 포인트 충전 실패로 인한 자동 취소");
            } catch (Exception cancelEx) {
                logger.error("NICE 충전 실패 후 토스 결제 취소도 실패 - paymentKey: {}", paymentKey, cancelEx);
                errMsg = "NICE 포인트 충전 실패, 토스 결제 취소도 실패: " + cancelEx.getMessage();
            }
            hisParams.put("procRstGbcd", PROC_RST_FAIL);
            hisParams.put("errCd", "NICE_FAIL");
            hisParams.put("errMsgCntn", errMsg);
            insertAutoChrgHis(hisParams);
            throw new IllegalStateException(errMsg);
        }

        hisParams.put("procRstGbcd", PROC_RST_SUCCESS);
        insertAutoChrgHis(hisParams);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo((String) billingInfo.get("mcustNo"));
        resVO.setBillKey((String) billingInfo.get("billKey"));
        resVO.setPaymentKey(paymentKey);
        resVO.setOrderId(orderId);
        resVO.setStatus((String) tossPayment.get("status"));
        resVO.setMessage("자동충전 결제 요청 완료");

        logger.debug("requestAutoCharge end");
        return resVO;
    }

    /**
     * 자동충전 결제 이력(AP_CUST_PNT_AUTO_CHRG_HIS) 등록.
     * params 키: mcustNo, paymentKey, orderId, chargeAmt(Integer), realChrgAmt(Integer),
     *            acntSeq(Integer), bankGbcd, acntNo, bankAcntNm, procRstGbcd, errMsgCntn, errCd(선택, 없으면 실패 시 "TOSS_FAIL")
     */
    private void insertAutoChrgHis(Map<String, Object> params) {
        String procRstGbcd = (String) params.get("procRstGbcd");
        String acntNo = (String) params.get("acntNo");
        String errMsgCntn = (String) params.get("errMsgCntn");

        PEAPntAutoChrgHisVO hisVO = new PEAPntAutoChrgHisVO();
        hisVO.setMcustNo((String) params.get("mcustNo"));
        hisVO.setPaymentKey((String) params.get("paymentKey"));
        hisVO.setOrderId((String) params.get("orderId"));
        hisVO.setChrgTypeGbcd(CHRG_TYPE_AUTO);
        hisVO.setRsvChrgMthdGbcd(RSV_GBCD_NA);
        hisVO.setRsvChrgPrdGbcd(RSV_GBCD_NA);
        hisVO.setAcntChrgAmt((Integer) params.get("chargeAmt"));
        hisVO.setRealChrgAmt((Integer) params.get("realChrgAmt"));
        hisVO.setAcntSeq((Integer) params.get("acntSeq"));
        hisVO.setBankGbcd((String) params.get("bankGbcd"));
        // Oracle은 빈 문자열('')을 NULL로 취급하므로 NOT NULL 컬럼엔 플레이스홀더("-")를 사용한다.
        // 계좌번호는 빌링키 발급 시점(issueBillingKey)에 저장되나, 그 이전에 등록된 계좌는 없을 수 있어 방어 처리.
        hisVO.setAcntNo(acntNo == null || acntNo.isEmpty() ? "-" : acntNo);
        hisVO.setBankAcntNm((String) params.get("bankAcntNm"));
        hisVO.setProcRstGbcd(procRstGbcd);
        // Oracle은 빈 문자열('')을 NULL로 취급하므로 NOT NULL 컬럼엔 플레이스홀더("-")를 사용한다.
        String errCd = (String) params.get("errCd");
        if (errCd == null) {
            errCd = PROC_RST_FAIL.equals(procRstGbcd) ? "TOSS_FAIL" : "-";
        }
        hisVO.setErrCd(errCd);
        hisVO.setErrMsgCntn(errMsgCntn == null ? "-" : truncate(errMsgCntn, 300));
        peaAutoChargeDAO.insertPntAutoChrgHis(hisVO);
    }

    /**
     * NICE 웹소켓 포인트 충전 요청. (전문코드 0105, docs/project/나이스페이-웹소켓-충전전문.md 참고)
     * TODO 회사에서 쓰는 NICE 충전 함수로 본문 교체 예정. 현재는 미구현 스텁.
     *
     * @return 충전 성공 여부 (true=성공, false=실패)
     */
    private boolean requestNiceCharge(String mcustNo, int chargeAmt) {
        // TODO 회사 NICE 충전 함수로 교체 필요
        return true;
    }

    private String truncate(String value, int maxLength) {
        if (value == null) {
            return "";
        }
        return value.length() > maxLength ? value.substring(0, maxLength) : value;
    }

    /** 자동충전 결제 취소 요청. */
    @Override
    public PEAAutoChargeResVO cancelAutoCharge(PEAAutoChargeReq.Cancel reqVO) throws Exception {
        logger.debug("cancelAutoCharge start");

        PayMethod payMethod = PayMethod.of(reqVO.getPayMethod());
        logger.debug("cancelAutoCharge payMethod: {}", payMethod);

        if (reqVO.getPaymentKey() == null || reqVO.getPaymentKey().isEmpty()) {
            throw new IllegalArgumentException("취소할 결제의 paymentKey가 없습니다.");
        }

        PEAPntAutoChrgHisVO originalHis =
                peaAutoChargeDAO.selectPntAutoChrgHisByPaymentKey(reqVO.getMcustNo(), reqVO.getPaymentKey());
        if (originalHis == null) {
            logger.warn("본인 결제가 아닌 paymentKey 취소 시도 - mcustNo: {}, paymentKey: {}",
                    reqVO.getMcustNo(), reqVO.getPaymentKey());
            throw new IllegalStateException("본인 결제가 아니거나 존재하지 않는 결제입니다.");
        }

        Map<String, Object> hisParams = new HashMap<>();
        hisParams.put("mcustNo", reqVO.getMcustNo());
        hisParams.put("paymentKey", reqVO.getPaymentKey());
        hisParams.put("orderId", originalHis.getOrderId());
        hisParams.put("chargeAmt", originalHis.getAcntChrgAmt());
        hisParams.put("realChrgAmt", 0);

        hisParams.put("acntSeq", originalHis.getAcntSeq());
        hisParams.put("bankGbcd", originalHis.getBankGbcd());
        hisParams.put("acntNo", originalHis.getAcntNo());
        hisParams.put("bankAcntNm", originalHis.getBankAcntNm());

        JSONObject tossCancel;
        try {
            tossCancel = requestTossPaymentCancel(reqVO.getPaymentKey(), reqVO.getCancelReason());
        } catch (Exception e) {
            hisParams.put("procRstGbcd", PROC_RST_FAIL);
            hisParams.put("errMsgCntn", e.getMessage());
            insertAutoChrgHis(hisParams);
            throw e;
        }

        hisParams.put("procRstGbcd", PROC_RST_CANCEL);
        insertAutoChrgHis(hisParams);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo(reqVO.getMcustNo());
        resVO.setPaymentKey(reqVO.getPaymentKey());
        resVO.setStatus((String) tossCancel.get("status"));
        resVO.setMessage("자동충전 결제 취소 완료");

        logger.debug("cancelAutoCharge end");
        return resVO;
    }

    /**
     * 토스 결제 취소 요청.
     * 참조: https://docs.tosspayments.com/guides/v2/cancel-payment
     */
    private JSONObject requestTossPaymentCancel(String paymentKey, String cancelReason) throws Exception {
        String secretKey = System.getenv(TOSS_SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("토스 시크릿키 환경변수(" + TOSS_SECRET_KEY_ENV + ")가 설정되지 않았습니다.");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("cancelReason", cancelReason == null || cancelReason.isEmpty()
                ? "자동충전 결제 취소"
                : cancelReason);

        String encodedPaymentKey = URLEncoder.encode(paymentKey, StandardCharsets.UTF_8);
        JSONObject responseBody = postJson(
                TOSS_PAYMENT_URL + encodedPaymentKey + "/cancel",
                secretKey,
                requestBody);

        int statusCode = (Integer) responseBody.get("statusCode");
        if (statusCode != 200) {
            String tossMessage = (String) responseBody.get("message");
            logger.error("토스 결제 취소 실패 - paymentKey: {}, status: {}, body: {}", paymentKey, statusCode, responseBody);
            throw new IllegalStateException("토스 결제 취소에 실패했습니다. (" + (tossMessage == null ? "사유 미상" : tossMessage) + ")");
        }

        logger.info("토스 결제 취소 완료 - paymentKey: {}", paymentKey);
        return responseBody;
    }

    /**
     * 등록 계좌의 빌링키 조회 및 검증.
     * 계좌 미등록 또는 빌링키가 없으면 예외를 던진다.
     */
    private Map<String, Object> validationBillingKey(String mcustNo) {
        if (mcustNo == null || mcustNo.isEmpty()) {
            logger.warn("빌링키 조회 요청값 없음");
            throw new IllegalStateException("등록된 계좌 또는 빌링키가 존재하지 않습니다.");
        }

        PEAAutoChargeVO acntVO = peaAutoChargeDAO.selectBillingKey(mcustNo);
        if (acntVO == null || acntVO.getBillKey() == null || acntVO.getBillKey().isEmpty()) {
            logger.warn("등록된 계좌/빌링키 없음 - mcustNo: {}", mcustNo);
            throw new IllegalStateException("등록된 계좌 또는 빌링키가 존재하지 않습니다.");
        }

        Map<String, Object> billingInfo = new HashMap<>();
        billingInfo.put("mcustNo", acntVO.getMcustNo());
        billingInfo.put("customerKey", acntVO.getCustKey());
        billingInfo.put("billKey", acntVO.getBillKey());
        billingInfo.put("bankGbcd", acntVO.getBankGbcd());
        billingInfo.put("acntSeq", acntVO.getAcntSeq());
        billingInfo.put("acntNo", acntVO.getAcntNo());
        billingInfo.put("bankAcntNm", acntVO.getBankAcntNm());

        logger.info("빌링키 조회 완료 - mcustNo: {}", mcustNo);
        return billingInfo;
    }

    /**
     * 토스 빌링키 자동결제 요청.
     * 참조: https://docs.tosspayments.com/guides/v2/billing/integration-api
     */
    private JSONObject requestTossBillingPayment(Map<String, Object> billingInfo, PEAAutoChargeReq.Charge reqVO, int chargeAmt)
            throws Exception {
        String billKey = (String) billingInfo.get("billKey");
        String customerKey = (String) billingInfo.get("customerKey");

        if (billKey == null || billKey.isEmpty()) {
            throw new IllegalStateException("토스 빌링키가 없습니다.");
        }
        if (customerKey == null || customerKey.isEmpty()) {
            throw new IllegalStateException("토스 customerKey가 없습니다.");
        }

        String secretKey = System.getenv(TOSS_SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("토스 시크릿키 환경변수(" + TOSS_SECRET_KEY_ENV + ")가 설정되지 않았습니다.");
        }

        String orderId = makeOrderId(reqVO);
        JSONObject requestBody = new JSONObject();
        requestBody.put("customerKey", customerKey);
        requestBody.put("amount", chargeAmt);
        requestBody.put("orderId", orderId);
        requestBody.put("orderName", reqVO.getOrderName() == null || reqVO.getOrderName().isEmpty()
                ? "자동충전"
                : reqVO.getOrderName());
        requestBody.put("customerEmail", reqVO.getCustomerEmail());
        requestBody.put("customerName", reqVO.getCustomerName());

        String encodedBillKey = URLEncoder.encode(billKey, StandardCharsets.UTF_8);
        JSONObject responseBody = postJson(
                TOSS_BILLING_PAYMENT_URL + encodedBillKey,
                secretKey,
                requestBody);

        int statusCode = (Integer) responseBody.get("statusCode");
        if (statusCode != 200) {
            logger.error("토스 빌링 결제 실패 - status: {}, body: {}", statusCode, responseBody);
            throw new IllegalStateException("토스 빌링 결제에 실패했습니다.");
        }




        logger.info("토스 빌링 결제 완료 - orderId: {}", orderId);
        return responseBody;
    }

    private String makeOrderId(PEAAutoChargeReq.Charge reqVO) {
        if (reqVO.getOrderId() != null && !reqVO.getOrderId().isEmpty()) {
            return reqVO.getOrderId();
        }
        return "AUTO_CHARGE_" + reqVO.getMcustNo() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    /**
     * 토스 API에 HTTP POST 요청을 보내고 JSON 응답을 파싱하여 반환한다.
     * 단위 테스트(같은 패키지)에서 직접 호출할 수 있도록 package-private으로 둔다.
     */
    JSONObject postJson(String urlStr, String secretKey, JSONObject requestBody) throws Exception {
        String auth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(urlStr))
                .timeout(Duration.ofSeconds(15))
                .header("Authorization", "Basic " + auth)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toJSONString(), StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response;
        try {
            response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (java.io.IOException e) {
            logger.error("토스 API 연결 실패 - url: {}", urlStr, e);
            throw new AutoChargeException("CONN_ERROR", "토스 서버와 통신 중 오류가 발생했습니다. (" + e.getMessage() + ")", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("토스 API 요청이 중단됨 - url: {}", urlStr, e);
            throw new AutoChargeException("CONN_ERROR", "토스 서버 요청이 중단되었습니다. (" + e.getMessage() + ")", e);
        }

        JSONObject responseBody = (JSONObject) new JSONParser().parse(response.body());
        responseBody.put("statusCode", response.statusCode());
        return responseBody;
    }
}
