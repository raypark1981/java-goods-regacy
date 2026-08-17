package hmmbl.pe.pea.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PEATossHttpUtil {

    /**
     * HTTP POST 요청을 보내고 JSON 응답을 파싱하여 반환한다.
     *
     * @param urlStr 요청 URL
     * @param secretKey Basic Auth에 사용할 시크릿 키
     * @param requestBody 전송할 JSON 바디
     * @return 파싱된 응답 JSONObject
     * @throws Exception HTTP 통신 또는 JSON 파싱 실패
     */
    // PEAAutoChargeServiceImpl로 인라인 처리하여 미사용. 공통 util로 안 뽑기로 함.
    // public static JSONObject postJson(String urlStr, String secretKey, JSONObject requestBody)
    //         throws Exception {
    //     URL url = new URL(urlStr);
    //     HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    //
    //     try {
    //         System.setProperty("https.protocols", "TLSv1.2");
    //         SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    //         sslContext.init(null, null, null);
    //         conn.setSSLSocketFactory(sslContext.getSocketFactory());
    //     } catch (NoSuchAlgorithmException | KeyManagementException e) {
    //         throw new RuntimeException("TLS1.2 설정 실패", e);
    //     }
    //
    //     String auth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
    //     conn.setRequestProperty("Authorization", "Basic " + auth);
    //     conn.setRequestProperty("Content-Type", "application/json");
    //     conn.setRequestMethod("POST");
    //     conn.setDoOutput(true);
    //
    //     try (OutputStream os = conn.getOutputStream()) {
    //         os.write(requestBody.toJSONString().getBytes(StandardCharsets.UTF_8));
    //     }
    //
    //     int statusCode = conn.getResponseCode();
    //
    //     try (InputStream is = statusCode == 200 ? conn.getInputStream() : conn.getErrorStream();
    //             Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
    //         JSONObject responseBody = (JSONObject) new JSONParser().parse(reader);
    //         responseBody.put("statusCode", statusCode);
    //         return responseBody;
    //     }
    // }
}
