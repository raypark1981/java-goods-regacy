package hmmbl.pe.pea.service.impl;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PEAAutoChargeServiceImplTest {

    // 토스 개발자센터 > API 키에서 확인한 테스트 시크릿 키
    private static final String TEST_SECRET_KEY = "test_sk_eqRGgYO1r5jywQJZqn518QnN2Eya";

    // 계좌 빌링키 발급 엔드포인트
    private static final String BILLING_ISSUE_URL =
            "https://api.tosspayments.com/v1/billing/authorizations/issue";

    // postJson()은 HttpServletRequest/DAO에 의존하지 않으므로 DAO는 null로 넘겨도 된다.
    private final PEAAutoChargeServiceImpl service = new PEAAutoChargeServiceImpl(null);

    @Test
    void postJson_호출_확인() throws Exception {
        JSONObject body = new JSONObject();
        body.put("authKey", "test_authKey");
        body.put("customerKey", "test_customer_001");

        JSONObject response = service.postJson(BILLING_ISSUE_URL, TEST_SECRET_KEY, body);

        System.out.println("statusCode : " + response.get("statusCode"));
        System.out.println("response   : " + response.toJSONString());

        assertNotNull(response);
        assertNotNull(response.get("statusCode"));
    }
}
