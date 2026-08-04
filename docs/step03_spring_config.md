# Step 03 — Spring 설정 XML (dispatcher-servlet.xml / applicationContext.xml)

## 실행 순서: Tomcat → Spring → DispatcherServlet

```
Tomcat 시작
  └── web.xml 읽기
        ├── ContextLoaderListener 실행
        │     └── applicationContext.xml 로드
        │           └── DB, MyBatis, Service, DAO Bean 생성  ← Root Context
        │
        └── DispatcherServlet 초기화 (load-on-startup: 1)
              └── dispatcher-servlet.xml 로드
                    └── Controller, ViewResolver Bean 생성   ← Servlet Context

요청이 들어오면
  └── Tomcat이 URL 패턴 확인 (web.xml)
        └── *.do → DispatcherServlet
              └── @RequestMapping 찾아서 Controller 실행
```

Root Context가 먼저 뜨고, 그 다음 Servlet Context가 뜬다.  
Servlet Context(자식)는 Root Context(부모) Bean을 참조할 수 있다.  
Controller가 Service를 주입받을 수 있는 이유가 이 부모/자식 관계 때문이다.

---

## 왜 설정 파일이 두 개인가?

Spring MVC 레거시 앱은 두 개의 ApplicationContext를 계층으로 운영한다.

```
Root ApplicationContext  (applicationContext.xml)
│   DB, Service, DAO, MyBatis, 트랜잭션
│   → Tomcat 시작 시 ContextLoaderListener가 초기화
│
└── Servlet ApplicationContext  (dispatcher-servlet.xml)
        Controller, ViewResolver, 정적 자원
        → DispatcherServlet이 초기화
        → 부모(Root)의 Bean을 참조 가능
```

자식(Servlet Context)은 부모(Root Context) Bean을 사용할 수 있지만,  
부모는 자식 Bean을 모른다 → Service가 Controller를 직접 참조하는 설계를 막아줌.

---

## dispatcher-servlet.xml 분석

### `<context:component-scan>`

```xml
<context:component-scan base-package="com.hyundaicard.mmall.controller"/>
```

지정한 패키지를 스캔해서 `@Controller` 붙은 클래스를 자동으로 Bean 등록.  
수동으로 `<bean>` 태그를 쓰지 않아도 된다.

### `<mvc:annotation-driven/>`

```xml
<mvc:annotation-driven/>
```

이 한 줄이 하는 일:
- `@RequestMapping`, `@GetMapping`, `@PostMapping` 활성화
- `@RequestParam`, `@PathVariable`, `@ResponseBody` 활성화
- JSON 변환기(Jackson) 자동 등록
- 유효성 검사(Validation) 연동

없으면 `@RequestMapping`이 동작하지 않는다.

### `<mvc:resources>`

```xml
<mvc:resources mapping="/resources/**" location="/resources/"/>
```

`/resources/css/style.css` 같은 요청은 DispatcherServlet이 Controller를 찾지 않고  
`webapp/resources/` 폴더의 파일을 직접 반환한다.

이 설정이 없으면 CSS/JS 파일 요청도 `*.do`가 아닌데 404가 발생할 수 있다.

### InternalResourceViewResolver

```xml
<bean class="...InternalResourceViewResolver">
    <property name="prefix" value="/WEB-INF/views/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```

Controller에서 문자열 `"goods/goodsDetail"` 을 반환하면:

```
prefix + 반환값 + suffix
= /WEB-INF/views/ + goods/goodsDetail + .jsp
= /WEB-INF/views/goods/goodsDetail.jsp
```

`/WEB-INF/views/` 안에 JSP를 두는 이유:  
WEB-INF는 브라우저가 직접 URL로 접근할 수 없다 → JSP를 직접 노출하지 않는 보안 설계.

---

## applicationContext.xml 분석

### DataSource (DB 연결 풀)

```xml
<bean id="dataSource" class="org.apache.commons.dbcp2.BasicDataSource">
    <property name="driverClassName" value="oracle.jdbc.OracleDriver"/>
    <property name="url" value="jdbc:oracle:thin:@localhost:1521:orcl"/>
    <property name="username" value="mmall"/>
    <property name="password" value="mmall123"/>
    <property name="initialSize" value="5"/>   <!-- 시작 시 연결 5개 -->
    <property name="maxTotal" value="20"/>      <!-- 최대 연결 20개 -->
</bean>
```

DBCP2가 DB 연결을 풀로 관리한다.  
요청이 오면 풀에서 연결 하나를 빌려주고, 작업 완료 후 반납.  
동시에 20명이 접속해도 연결을 재사용하므로 성능이 좋다.

### SqlSessionFactory (MyBatis 핵심)

```xml
<bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
    <property name="dataSource" ref="dataSource"/>
    <property name="mapperLocations" value="classpath:mapper/**/*.xml"/>
    <property name="configuration">
        <bean class="org.apache.ibatis.session.Configuration">
            <property name="mapUnderscoreToCamelCase" value="true"/>
        </bean>
    </property>
</bean>
```

- `mapperLocations` : `src/main/resources/mapper/` 아래 모든 XML SQL 파일을 읽음
- `mapUnderscoreToCamelCase` : DB 컬럼 `GD_CD` → Java 필드 `gdCd` 자동 변환

### MapperScannerConfigurer (Mapper 자동 등록)

```xml
<bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
    <property name="basePackage" value="com.hyundaicard.mmall.dao"/>
</bean>
```

`dao` 패키지의 인터페이스에 `@Mapper`만 붙이면 자동으로 Bean이 된다.  
구현 클래스를 직접 작성하지 않아도 MyBatis가 프록시 객체를 생성해준다.

### 트랜잭션 관리자

```xml
<bean id="transactionManager"
      class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
    <property name="dataSource" ref="dataSource"/>
</bean>
<tx:annotation-driven transaction-manager="transactionManager"/>
```

Service 메서드에 `@Transactional`을 붙이면 자동으로 트랜잭션이 시작/커밋/롤백된다.

```java
@Transactional
public void placeOrder(OrderVO order) {
    orderDao.insert(order);       // 실패하면
    inventoryDao.decrease(order); // 이 줄도 함께 롤백
}
```

---

## @Controller 가 @Service 를 찾는 흐름

```java
@Controller
public class GoodsController {

    private final GoodsService goodsService;

    public GoodsController(GoodsService goodsService) { // 생성자 주입
        this.goodsService = goodsService;
    }
}
```

Spring 시작 시 동작 순서:

```
1. applicationContext.xml → @Service 스캔 → GoodsService Bean 생성
2. dispatcher-servlet.xml → @Controller 스캔 → GoodsController Bean 생성 시도
3. 생성자에 GoodsService 필요하다는 걸 인식
4. 부모 컨텍스트(Root)에서 GoodsService Bean 찾아서 자동 주입
5. GoodsController Bean 완성
```

Web Context(자식)는 Root Context(부모) Bean을 참조할 수 있기 때문에 가능하다.
반대로 Service가 Controller를 참조하는 것은 불가능하다 → 의존성 방향을 강제하는 설계.

| 어노테이션 | 스캔 위치 | 역할 |
|---|---|---|
| `@Controller` | `dispatcher-servlet.xml` | 웹 요청 처리 Bean |
| `@Service` | `applicationContext.xml` | 비즈니스 로직 Bean |
| `@Mapper` | `applicationContext.xml` | DB 접근 Bean (MyBatis) |

---

## 정리

```
dispatcher-servlet.xml          applicationContext.xml
─────────────────────────────   ──────────────────────────────
웹 계층 담당                     비즈니스/데이터 계층 담당
@Controller 스캔                 @Service, @Repository 스캔
ViewResolver (JSP 경로 변환)     DataSource (DB 연결)
정적 자원 처리 (CSS/JS)          SqlSessionFactory (MyBatis)
                                 트랜잭션 관리자
```
