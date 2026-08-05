package hmfrnt.web;

import java.io.Serializable;

/**
 * 전체 VO / DTO 의 공통 베이스.
 * Serializable을 구현해 세션 직렬화, 캐시 저장에 대응한다.
 */
public abstract class BaseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public String toString() {
        return getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}
