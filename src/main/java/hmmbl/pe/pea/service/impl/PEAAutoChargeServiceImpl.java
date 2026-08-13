package hmmbl.pe.pea.service.impl;

import hmmbl.pe.pea.service.PEAAutoChargeService;
import hmmbl.pe.pea.service.dao.PEAAutoChargeDAO;
import hmmbl.pe.pea.util.PEATossHttpUtil;
import hmmbl.pe.pea.vo.PEAAutoChargeReqVO;
import hmmbl.pe.pea.vo.PEAAutoChargeResVO;
import hmmbl.pe.pea.vo.PEAAutoChargeVO;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Service
public class PEAAutoChargeServiceImpl implements PEAAutoChargeService {

    private static final Logger logger = LoggerFactory.getLogger(PEAAutoChargeServiceImpl.class);
    private static final String TOSS_BILLING_AUTH_URL    = "https://api.tosspayments.com/v1/billing/authorizations/issue";
    private static final String TOSS_BILLING_PAYMENT_URL = "https://api.tosspayments.com/v1/billing/";
    private static final String MOCK_BILL_KEY = "mock_billing_key";
    private static final String TOSS_SECRET_KEY_ENV = "TOSS_SECRET_KEY";

    private final PEAAutoChargeDAO peaAutoChargeDAO;

    public PEAAutoChargeServiceImpl(PEAAutoChargeDAO peaAutoChargeDAO) {
        this.peaAutoChargeDAO = peaAutoChargeDAO;
    }

    /** 빌링키 발급 및 저장. */
    @Override
    public PEAAutoChargeResVO issueBillingKey(PEAAutoChargeReqVO reqVO) throws Exception {
        logger.debug("issueBillingKey start");

        String secretKey = System.getenv(TOSS_SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("토스 시크릿키 환경변수(" + TOSS_SECRET_KEY_ENV + ")가 설정되지 않았습니다.");
        }

        JSONObject requestBody = new JSONObject();
        requestBody.put("authKey",     reqVO.getAuthKey());
        requestBody.put("customerKey", reqVO.getCustomerKey());

        JSONObject tossRes = PEATossHttpUtil.postJson(TOSS_BILLING_AUTH_URL, secretKey, requestBody);

        int statusCode = (Integer) tossRes.get("statusCode");
        if (statusCode < 200 || statusCode >= 300) {
            logger.error("토스 빌링키 발급 실패 - status: {}, body: {}", statusCode, tossRes);
            throw new IllegalStateException("토스 빌링키 발급에 실패했습니다.");
        }

        String billingKey = (String) tossRes.get("billingKey");
        logger.info("토스 빌링키 발급 완료 - mcustNo: {}", reqVO.getMcustNo());

        PEAAutoChargeVO acntVO = new PEAAutoChargeVO();
        acntVO.setMcustNo(reqVO.getMcustNo());
        acntVO.setCustKey(reqVO.getCustomerKey());
        acntVO.setBillKey(billingKey);
        peaAutoChargeDAO.insertChrgAcnt(acntVO);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo(reqVO.getMcustNo());

        logger.debug("issueBillingKey end");
        return resVO;
    }

    /** 자동충전 결제 요청. */
    @Override
    public PEAAutoChargeResVO requestAutoCharge(PEAAutoChargeReqVO reqVO) throws Exception {
        logger.debug("requestAutoCharge start");

        Map<String, Object> billingInfo = validationBillingKey(reqVO);
        JSONObject tossPayment = requestTossBillingPayment(billingInfo, reqVO);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo((String) billingInfo.get("mcustNo"));
        resVO.setBillKey((String) billingInfo.get("billKey"));
        resVO.setPaymentKey((String) tossPayment.get("paymentKey"));
        resVO.setOrderId((String) tossPayment.get("orderId"));
        resVO.setStatus((String) tossPayment.get("status"));
        resVO.setMessage("자동충전 결제 요청 완료");

        logger.debug("requestAutoCharge end");
        return resVO;
    }

    /** 자동충전 결제 취소 요청. */
    @Override
    public PEAAutoChargeResVO cancelAutoCharge(PEAAutoChargeReqVO reqVO) throws Exception {
        logger.debug("cancelAutoCharge start");

        Map<String, Object> billingInfo = validationBillingKey(reqVO);

        PEAAutoChargeResVO resVO = new PEAAutoChargeResVO();
        resVO.setMcustNo((String) billingInfo.get("mcustNo"));
        resVO.setBillKey((String) billingInfo.get("billKey"));
        resVO.setMessage("CANCEL_SUCCESS");

        logger.debug("cancelAutoCharge end");
        return resVO;
    }

    /**
     * 등록 계좌의 빌링키 조회 및 검증.
     * 계좌 미등록 또는 빌링키가 없으면 예외를 던진다.
     */
    private Map<String, Object> validationBillingKey(PEAAutoChargeReqVO reqVO) {
        if (reqVO == null || reqVO.getMcustNo() == null || reqVO.getMcustNo().isEmpty()) {
            logger.warn("빌링키 조회 요청값 없음");
            throw new IllegalStateException("등록된 계좌 또는 빌링키가 존재하지 않습니다.");
        }

        Map<String, Object> billingInfo = new HashMap<>();
        billingInfo.put("mcustNo", reqVO.getMcustNo());
        billingInfo.put("customerKey", "mock_customer_" + reqVO.getMcustNo());
        billingInfo.put("billKey", MOCK_BILL_KEY);
        billingInfo.put("bankGbcd", "088");
        billingInfo.put("acntSeq", 1);

        logger.info("빌링키 mock 조회 완료 - mcustNo: {}", reqVO.getMcustNo());
        return billingInfo;
    }

    /**
     * 토스 빌링키 자동결제 요청.
     */
    private JSONObject requestTossBillingPayment(Map<String, Object> billingInfo, PEAAutoChargeReqVO reqVO)
            throws Exception {
        String billKey = (String) billingInfo.get("billKey");
        String customerKey = (String) billingInfo.get("customerKey");

        if (billKey == null || billKey.isEmpty()) {
            throw new IllegalStateException("토스 빌링키가 없습니다.");
        }
        if (customerKey == null || customerKey.isEmpty()) {
            throw new IllegalStateException("토스 customerKey가 없습니다.");
        }
        if (reqVO.getChargeAmt() <= 0) {
            throw new IllegalArgumentException("충전금액은 0보다 커야 합니다.");
        }

        String secretKey = System.getenv(TOSS_SECRET_KEY_ENV);
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalStateException("토스 시크릿키 환경변수(" + TOSS_SECRET_KEY_ENV + ")가 설정되지 않았습니다.");
        }

        String orderId = makeOrderId(reqVO);
        JSONObject requestBody = new JSONObject();
        requestBody.put("customerKey", customerKey);
        requestBody.put("amount", reqVO.getChargeAmt());
        requestBody.put("orderId", orderId);
        requestBody.put("orderName", reqVO.getOrderName() == null || reqVO.getOrderName().isEmpty()
                ? "자동충전"
                : reqVO.getOrderName());
        requestBody.put("customerEmail", reqVO.getCustomerEmail());
        requestBody.put("customerName", reqVO.getCustomerName());

        String encodedBillKey = URLEncoder.encode(billKey, StandardCharsets.UTF_8);
        JSONObject responseBody = PEATossHttpUtil.postJson(
                TOSS_BILLING_PAYMENT_URL + encodedBillKey,
                secretKey,
                requestBody);

        int statusCode = (Integer) responseBody.get("statusCode");
        if (statusCode < 200 || statusCode >= 300) {
            logger.error("토스 빌링 결제 실패 - status: {}, body: {}", statusCode, responseBody);
            throw new IllegalStateException("토스 빌링 결제에 실패했습니다.");
        }

        logger.info("토스 빌링 결제 완료 - orderId: {}", orderId);
        return responseBody;
    }

    private String makeOrderId(PEAAutoChargeReqVO reqVO) {
        if (reqVO.getOrderId() != null && !reqVO.getOrderId().isEmpty()) {
            return reqVO.getOrderId();
        }
        return "AUTO_CHARGE_" + reqVO.getMcustNo() + "_"
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}
