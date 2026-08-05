package hmfrnt.web;

import hmfrnt.common.HdgmMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 전체 Controller의 공통 베이스 클래스.
 * 세션 처리, 공통 파라미터 바인딩 등 운영 프레임워크 공통 기능을 제공한다.
 */
public abstract class BaseController {

    /** 세션에서 글로벌 언어 코드를 가져온다 (ko / en / zh 등). */
    protected String getSessionGlobLang() {
        // 실제 구현 시 HttpSession에서 globLang 속성을 꺼낸다.
        return "ko";
    }

    /** 공통 응답 Map 생성. */
    protected HdgmMap newResponseMap() {
        return new HdgmMap();
    }

    /**
     * 운영에서는 res.send(redirect:…) 패턴으로 리다이렉트를 처리한다.
     * 여기서는 Spring redirect: prefix를 반환하는 헬퍼로 대체한다.
     */
    protected String redirect(String path) {
        return "redirect:" + path;
    }
}
