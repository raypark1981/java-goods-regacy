# Step 02 — web.xml (웹 애플리케이션 진입점)

## web.xml이란?

서블릿 컨테이너(Tomcat)가 가장 먼저 읽는 설정 파일이다.  
"이 웹앱을 어떻게 초기화하고, 어떤 URL을 누가 처리하는가"를 Tomcat에게 알려준다.  
Spring Boot에서는 이 파일이 없어졌지만, 레거시 Spring MVC에서는 필수다.

위치: `src/main/webapp/WEB-INF/web.xml`  
(WEB-INF 안에 있으면 외부에서 직접 URL로 접근 불가 → 보안상 올바른 위치)

---

## 구성 요소 4가지

### 1. Root ApplicationContext (전체 공통 Bean)

```xml
<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/spring/applicationContext.xml</param-value>
</context-param>
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>
```

- `ContextLoaderListener` : Tomcat이 시작될 때 Spring 컨텍스트를 초기화하는 리스너
- `applicationContext.xml` : DB 연결, MyBatis, Service 같은 **공통 Bean** 설정
- 이 컨텍스트는 애플리케이션 전체에서 공유된다

**왜 두 개의 컨텍스트가 필요한가?**

```
Root Context (applicationContext.xml)
└── DB, Service, DAO, MyBatis  ← 공통. 어디서든 접근 가능

Servlet Context (dispatcher-servlet.xml)
└── Controller, ViewResolver   ← 웹 계층 전용
```

Controller는 Service를 호출해야 하므로 Root Context의 Bean을 참조한다.  
반대로 Service는 Controller를 알 필요가 없다 → 관심사 분리.

---

### 2. CharacterEncodingFilter (한글 깨짐 방지)

```xml
<filter>
    <filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <url-pattern>/*</url-pattern>  <!-- 모든 요청에 적용 -->
</filter-mapping>
```

HTTP 요청이 Controller에 도달하기 전에 필터가 먼저 실행된다.  
`/*` 패턴으로 모든 요청에 UTF-8 인코딩을 강제 적용 → 한글 파라미터 깨짐 방지.

요청 처리 흐름:
```
브라우저 요청 → Filter(인코딩) → DispatcherServlet → Controller
```

---

### 3. DispatcherServlet (Spring MVC의 핵심)

```xml
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/dispatcher-servlet.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>*.do</url-pattern>   <!-- ← 이게 .do 패턴의 정체 -->
</servlet-mapping>
```

**DispatcherServlet이 하는 일:**

```
*.do 요청 수신
    ↓
HandlerMapping : URL에 맞는 Controller 메서드 찾기
    ↓
HandlerAdapter : Controller 메서드 실행
    ↓
ModelAndView 반환 (데이터 + 뷰 이름)
    ↓
ViewResolver : 뷰 이름 → 실제 JSP 파일 경로 변환
    ↓
JSP 렌더링 → 브라우저에 HTML 응답
```

**`<load-on-startup>1</load-on-startup>`**  
숫자가 낮을수록 먼저 초기화. `1`이면 Tomcat 시작 시 즉시 초기화.  
이 설정이 없으면 첫 번째 요청이 들어올 때 초기화 → 첫 응답이 느려짐.

**`.do` 패턴의 이유:**  
`/goods/goodsDetail.do` → `*.do`에 매핑된 DispatcherServlet이 처리  
`/resources/css/style.css` → `.do`가 아니므로 DispatcherServlet이 처리하지 않음 (정적 파일 그대로 반환)

---

### 4. welcome-file-list

```xml
<welcome-file-list>
    <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

루트 URL(`/`) 접속 시 보여줄 기본 파일.

---

## 전체 요청 흐름 요약

```
브라우저: GET /goods/goodsDetail.do?gdCd=2735991
    ↓
Tomcat: *.do 패턴 → DispatcherServlet으로 전달
    ↓
DispatcherServlet: /goods/goodsDetail 경로에 매핑된 Controller 탐색
    ↓
GoodsController.goodsDetail() 실행 → 상품 데이터 조회
    ↓
ModelAndView("goods/goodsDetail") 반환
    ↓
ViewResolver: /WEB-INF/views/goods/goodsDetail.jsp 로 변환
    ↓
JSP 렌더링 → HTML 응답
```

---

## 정리

```
web.xml 구성
├── ContextLoaderListener  → 공통 Bean (DB, Service) 초기화
├── CharacterEncodingFilter → 한글 UTF-8 처리
├── DispatcherServlet       → *.do 요청 처리 (Spring MVC 진입점)
└── welcome-file            → 기본 페이지
```
