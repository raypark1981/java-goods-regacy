# Step 04 — MVC 레이어 구조 (Controller / Service / DAO / Mapper)

## Spring MVC 계층 구조

레거시 Spring MVC는 역할에 따라 코드를 4개 계층으로 나눈다.

```
브라우저 요청
    ↓
Controller  (@Controller)   — 요청 수신, 응답 결정
    ↓
Service     (@Service)      — 비즈니스 로직, 트랜잭션
    ↓
DAO/Mapper  (@Mapper)       — DB 접근 (SQL 호출)
    ↓
DB (Oracle)
```

각 계층은 바로 아래 계층만 호출한다.  
Controller가 DAO를 직접 부르면 안 된다 → 유지보수성, 테스트 용이성 때문.

---

## 1. VO (Value Object) — `GoodsVO.java`

```java
@Getter @Setter @ToString
public class GoodsVO {
    private String gdCd;
    private String gdNm;
    private int    gdPrice;
    ...
}
```

DB 테이블 한 행의 데이터를 담는 그릇.  
Lombok의 `@Getter/@Setter`로 getter/setter 자동 생성 → 코드 수십 줄 절약.

**mapUnderscoreToCamelCase 설정 효과:**
```
DB 컬럼   → Java 필드
GD_CD    → gdCd
GD_NM    → gdNm
GD_PRICE → gdPrice
```
MyBatis가 자동으로 매핑해줘서 `resultMap`을 직접 작성하지 않아도 된다.

---

## 2. Mapper 인터페이스 — `GoodsMapper.java`

```java
@Mapper
public interface GoodsMapper {
    GoodsVO selectGoodsDetail(@Param("gdCd") String gdCd);
    List<GoodsVO> selectGoodsList();
}
```

인터페이스만 선언하면 MyBatis가 실제 구현 객체를 만들어준다.  
`@Mapper` → `MapperScannerConfigurer`가 자동으로 Bean 등록.  
`@Param("gdCd")` → XML SQL에서 `#{gdCd}` 로 참조할 파라미터 이름 지정.

---

## 3. MyBatis SQL XML — `GoodsMapper.xml`

```xml
<mapper namespace="com.hyundaicard.mmall.dao.GoodsMapper">

    <select id="selectGoodsDetail" parameterType="String"
            resultType="com.hyundaicard.mmall.model.GoodsVO">
        SELECT GD_CD, GD_NM, GD_PRICE
        FROM   TB_GOODS
        WHERE  GD_CD = #{gdCd}
        AND    USE_YN = 'Y'
    </select>

</mapper>
```

| 속성 | 설명 |
|------|------|
| `namespace` | Mapper 인터페이스 경로와 **완전히** 일치해야 함 |
| `id` | Mapper 인터페이스의 메서드명과 일치 |
| `parameterType` | 입력 파라미터 타입 |
| `resultType` | 결과를 담을 VO 클래스 |
| `#{gdCd}` | PreparedStatement의 `?`와 같음 (SQL Injection 방지) |

`<sql id="goodsColumns">` + `<include refid="goodsColumns"/>` :  
공통 SELECT 컬럼을 한 곳에 정의해서 여러 쿼리에서 재사용.

---

## 4. Service — `GoodsService.java`

```java
@Service
@Transactional(readOnly = true)
public class GoodsService {

    private final GoodsMapper goodsMapper;

    public GoodsService(GoodsMapper goodsMapper) {  // 생성자 주입
        this.goodsMapper = goodsMapper;
    }

    public GoodsVO getGoodsDetail(String gdCd) {
        return goodsMapper.selectGoodsDetail(gdCd);
    }
}
```

**생성자 주입 (Constructor Injection)을 쓰는 이유:**
- 필드가 `final`로 선언 가능 → 불변성 보장
- 테스트 시 Mock 주입이 명확
- Spring이 권장하는 방식

**`@Transactional(readOnly = true)`:**
- 조회 전용 트랜잭션 → DB에 불필요한 Lock을 걸지 않아 성능 향상
- 쓰기가 필요한 메서드에는 `@Transactional`(readOnly 없이) 별도 적용

---

## 5. Controller — `GoodsController.java`

```java
@Controller
@RequestMapping("/goods")
public class GoodsController {

    @GetMapping("/goodsDetail.do")
    public String goodsDetail(@RequestParam("gdCd") String gdCd, Model model) {
        GoodsVO goods = goodsService.getGoodsDetail(gdCd);
        model.addAttribute("goods", goods);
        return "goods/goodsDetail";   // ViewResolver → /WEB-INF/views/goods/goodsDetail.jsp
    }
}
```

| 어노테이션 | 역할 |
|-----------|------|
| `@Controller` | 이 클래스가 Controller임을 표시, Bean 등록 |
| `@RequestMapping("/goods")` | 클래스 레벨 공통 URL prefix |
| `@GetMapping("/goodsDetail.do")` | GET /goods/goodsDetail.do 요청 처리 |
| `@RequestParam("gdCd")` | URL 파라미터 `?gdCd=xxx` 값 추출 |
| `Model` | JSP로 데이터를 전달하는 Map |

**반환값 `"goods/goodsDetail"`의 여정:**
```
Controller 반환: "goods/goodsDetail"
    ↓  ViewResolver
prefix: /WEB-INF/views/
    +
반환값: goods/goodsDetail
    +
suffix: .jsp
    =
최종: /WEB-INF/views/goods/goodsDetail.jsp
```

---

## 6. JSP — `goodsDetail.jsp`

```jsp
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<h1>${goods.gdNm}</h1>
<fmt:formatNumber value="${goods.gdPrice}" type="number"/> 원
```

- `${goods.gdNm}` → model에 담긴 `GoodsVO.getGdNm()` 호출 (EL 표현식)
- `<c:choose>/<c:when>/<c:otherwise>` → if/else
- `<fmt:formatNumber>` → 숫자에 천단위 콤마 자동 포맷 (1000 → 1,000)

---

## 전체 흐름 한눈에 보기

```
GET /goods/goodsDetail.do?gdCd=2735991
    ↓
DispatcherServlet (web.xml *.do 매핑)
    ↓
GoodsController.goodsDetail("2735991", model)
    ↓
GoodsService.getGoodsDetail("2735991")
    ↓
GoodsMapper.selectGoodsDetail("2735991")
    ↓
GoodsMapper.xml <select id="selectGoodsDetail">
    → SELECT ... FROM TB_GOODS WHERE GD_CD = '2735991'
    ↓
GoodsVO { gdCd="2735991", gdNm="상품명", gdPrice=50000, ... }
    ↓
model.addAttribute("goods", goodsVO)
    ↓
ViewResolver → /WEB-INF/views/goods/goodsDetail.jsp
    ↓
${goods.gdNm} 렌더링 → HTML 응답
```
