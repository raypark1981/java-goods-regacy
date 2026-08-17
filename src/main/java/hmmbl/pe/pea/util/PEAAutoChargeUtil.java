package hmmbl.pe.pea.util;

/**
 * 자동충전 금액 산정 유틸.
 * 참고: docs/project/자동충전-금액-노출기준.md
 */
public final class PEAAutoChargeUtil {

    private static final int CHARGE_UNIT   = 10_000; // 1만 원 단위 올림
    private static final int MIN_CHARGE_AMT = 200;    // 최소 충전 가능 금액

    private PEAAutoChargeUtil() {
    }

    /**
     * 부족금액을 1만 원 단위로 올림하여 자동충전 금액을 산정한다.
     * 부족금액이 최소 충전 가능 금액(200원) 미만이면 충전 불가로 예외를 던진다.
     * 산정된 충전금액이 최대 충전 가능 금액을 초과하면 예외를 던진다.
     *
     * 예) 부족금액 26,200원 -> 30,000원 / 부족금액 1,200원 -> 10,000원
     *
     * @param shortageAmt  부족금액(요청금액)
     * @param maxChargeAmt 최대 충전 가능 금액
     * @return 1만 원 단위로 올림된 충전금액
     */
    public static int calculateChargeAmount(int shortageAmt, int maxChargeAmt) {
        if (shortageAmt < MIN_CHARGE_AMT) {
            throw new IllegalArgumentException(
                    "충전 가능 금액이 최소 기준(" + MIN_CHARGE_AMT + "원) 미만입니다.");
        }
        int chargeAmt = (int) (Math.ceil((double) shortageAmt / CHARGE_UNIT) * CHARGE_UNIT);
        if (chargeAmt > maxChargeAmt) {
            throw new IllegalArgumentException(
                    "충전 금액이 최대 한도(" + maxChargeAmt + "원)를 초과합니다.");
        }
        return chargeAmt;
    }
}
