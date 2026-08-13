package hmmbl.pe.pea.vo;

import hmfrnt.web.BaseVo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public class TossStlmBnftReqVO extends BaseVo {

    private static final long serialVersionUID = 1L;

    private String qryGbcd; // 조회 구분 코드

    @Getter
    @AllArgsConstructor
    public enum QryGbcd {
        PREP_CHRG("00", "선불충전"),
        BC_PREX("02", "현대백화점 상품권");

        private final String code;
        private final String codeName;

        private static final Map<String, QryGbcd> CODES =
                Collections.unmodifiableMap(Stream.of(values())
                        .collect(Collectors.toMap(QryGbcd::getCode, Function.identity())));

        public static QryGbcd fromCode(String code) {
            return Optional.ofNullable(CODES.get(code)).get();
        }
    }
}
