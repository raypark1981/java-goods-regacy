package hmfrnt.common;

/**
 * XSS(Cross-Site Scripting) 방어 유틸.
 * 운영에서는 Lucy XSS Filter 또는 자체 필터를 사용하며,
 * 여기서는 기본 이스케이프만 제공한다.
 */
public class XssUtil {

    private XssUtil() {}

    /** 입력값의 HTML 특수문자를 이스케이프한다. */
    public static String cleanXSS(String value) {
        if (value == null) return "";
        return value
                .replaceAll("&",  "&amp;")
                .replaceAll("<",  "&lt;")
                .replaceAll(">",  "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'",  "&#x27;");
    }
}
