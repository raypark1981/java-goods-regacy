package hmmbl.pe.pea;

import lombok.Getter;

/**
 * 결제수단 enum 정의.
 */
@Getter
public enum PayMethod {

    TRANSFER("TRANSFER", "96", "107101", "transfer", PropertyGroup.SIMP),
    CARD("CARD", "01", "107001", "card", PropertyGroup.CARD),
    KAKAOPAY("KAKAOPAY", "10", "107002", "card", PropertyGroup.SIMP),
    NAVERPAY("NAVERPAY", "11", "107003", "card", PropertyGroup.SIMP);

    private final String code;
    private final String prepChrgStlmGbcd;
    private final String tranTypeRsnCd;
    private final String responseKey;
    private final PropertyGroup group;

    PayMethod(String code, String prepChrgStlmGbcd, String tranTypeRsnCd, String responseKey, PropertyGroup group) {
        this.code = code;
        this.prepChrgStlmGbcd = prepChrgStlmGbcd;
        this.tranTypeRsnCd = tranTypeRsnCd;
        this.responseKey = responseKey;
        this.group = group;
    }

    /**
     * 문자열(payMethod)로 enum을 찾는 유틸 메서드.
     */
    public static PayMethod of(String code) {
        for (PayMethod pm : values()) {
            if (pm.code.equalsIgnoreCase(code)) {
                return pm;
            }
        }
        throw new IllegalArgumentException("Unknown PayMethod: " + code);
    }

    /**
     * 중복되는 prefix를 묶어 놓은 inner enum.
     */
    @Getter
    public enum PropertyGroup {
        CARD("toss.pg.card"),
        SIMP("toss.pg.simp");

        private final String prefix;

        PropertyGroup(String prefix) {
            this.prefix = prefix;
        }
    }

    public String getMidProperty() {
        return group.getPrefix() + ".mid";
    }

    public String getClientKeyProperty() {
        return group.getPrefix() + ".clientKey";
    }

    public String getSecretKeyProperty() {
        return group.getPrefix() + ".secretKey";
    }
}
