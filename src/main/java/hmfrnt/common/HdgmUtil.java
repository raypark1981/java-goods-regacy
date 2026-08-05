package hmfrnt.common;

/**
 * 운영 프레임워크 공통 유틸리티.
 * 언어 코드 처리, 경로 변환 등 전역에서 사용되는 함수를 제공한다.
 */
public class HdgmUtil {

    private HdgmUtil() {}

    /**
     * 언어코드(lang)와 요청 경로(path)를 받아
     * 글로벌 서비스 경로 prefix를 붙인 최종 경로를 반환한다.
     * - ko(한국어): 그대로 반환
     * - zh(중국어): /cn prefix 추가
     * - en(영어):   /en prefix 추가
     */
    public static String getLangByPath(String lang, String path) {
        String returnPath = "";
        lang = XssUtil.cleanXSS(lang);

        String[] arrayPath = path.split("/");
        for (int i = 0; i < arrayPath.length - 1; i++) {
            if (arrayPath[i].isEmpty()) continue;

            if (isKorea(lang)) {
                returnPath += arrayPath[i];
            } else if (isChina(lang)) {
                String[] arr = arrayPath[i].split("\\.");
                returnPath = arr[0] + "." + arr[1];
                returnPath = "/cn" + returnPath;
            } else if (isEnglish(lang)) {
                String[] arr = arrayPath[i].split("\\.");
                returnPath = arr[0] + "." + arr[1];
                returnPath = "/en" + returnPath;
            } else {
                returnPath += arrayPath[i] + "/";
            }
        }
        return returnPath;
    }

    private static boolean isKorea(String lang)   { return "ko".equals(lang); }
    private static boolean isChina(String lang)    { return "zh".equals(lang); }
    private static boolean isEnglish(String lang)  { return "en".equals(lang); }

    /** ASCII → 유니코드 변환 (운영 코드에서 convertAsciiToWCml 패턴으로 사용됨). */
    public static String convertAsciiToWcml(String value) {
        if (value == null) return "";
        StringBuilder sb = new StringBuilder();
        for (char c : value.toCharArray()) {
            if (c > 127) sb.append("&#").append((int) c).append(";");
            else         sb.append(c);
        }
        return sb.toString();
    }
}
