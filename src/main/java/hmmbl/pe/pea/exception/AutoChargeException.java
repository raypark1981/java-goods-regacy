package hmmbl.pe.pea.exception;

/**
 * 자동충전 처리 중 발생하는 예외. 이력(AP_CUST_PNT_AUTO_CHRG_HIS) 저장 시 ERR_CD로 그대로 쓸 수 있게
 * 에러코드를 메시지와 함께 들고 다닌다.
 */
public class AutoChargeException extends RuntimeException {

    private final String errCd;

    public AutoChargeException(String errCd, String message) {
        super(message);
        this.errCd = errCd;
    }

    public AutoChargeException(String errCd, String message, Throwable cause) {
        super(message, cause);
        this.errCd = errCd;
    }

    public String getErrCd() {
        return errCd;
    }
}
