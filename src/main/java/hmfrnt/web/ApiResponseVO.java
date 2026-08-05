package hmfrnt.web;

/**
 * API 공통 응답 래퍼.
 * 운영에서 JSON API 응답 시 result / message / data 구조로 통일한다.
 */
public class ApiResponseVO<T> {

    private String result;   // SUCCESS / FAIL
    private String message;
    private T      data;

    public static <T> ApiResponseVO<T> success(T data) {
        ApiResponseVO<T> vo = new ApiResponseVO<>();
        vo.result  = "SUCCESS";
        vo.message = "";
        vo.data    = data;
        return vo;
    }

    public static <T> ApiResponseVO<T> fail(String message) {
        ApiResponseVO<T> vo = new ApiResponseVO<>();
        vo.result  = "FAIL";
        vo.message = message;
        return vo;
    }

    public String getResult()  { return result; }
    public String getMessage() { return message; }
    public T      getData()    { return data; }
}
