package hmfrnt.common;

import java.util.HashMap;

/**
 * 운영 프레임워크 공통 Map 타입.
 * Controller ↔ Service ↔ DAO 간 파라미터/결과 전달에 사용된다.
 * HashMap을 상속하여 putAll, get 등 Map API를 그대로 사용할 수 있다.
 */
public class HdgmMap extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;

    public HdgmMap() {
        super();
    }

    public HdgmMap(int initialCapacity) {
        super(initialCapacity);
    }

    /** 값을 String으로 꺼낸다. null이면 빈 문자열 반환. */
    public String getString(String key) {
        Object val = get(key);
        return val == null ? "" : val.toString();
    }

    /** 값을 int로 꺼낸다. 없거나 변환 불가시 0 반환. */
    public int getInt(String key) {
        Object val = get(key);
        if (val == null) return 0;
        try { return Integer.parseInt(val.toString()); }
        catch (NumberFormatException e) { return 0; }
    }
}
