# Step 01 — pom.xml 구성 (Maven 프로젝트 설정)

## 왜 pom.xml이 중요한가?

Maven 프로젝트의 핵심 설정 파일이다.  
"이 프로젝트가 어떤 라이브러리를 쓰고, 어떻게 빌드하는가"를 정의한다.  
npm의 `package.json`과 같은 역할이라고 생각하면 된다.

---

## 핵심 변경 1 — `<packaging>war</packaging>`

```xml
<packaging>war</packaging>
```

| packaging | 결과물 | 용도 |
|-----------|--------|------|
| `jar` (기본값) | 실행 가능한 .jar | 일반 Java 앱, Spring Boot |
| `war` | .war 파일 | **Tomcat 같은 WAS에 배포하는 레거시 웹앱** |

`.do` URL을 처리하는 레거시 Spring MVC 앱은 반드시 `war`여야 한다.  
jar로 두면 `webapp/` 폴더 자체가 빌드 결과에 포함되지 않는다.

---

## 핵심 변경 2 — `<scope>` 개념

의존성에는 범위(scope)가 있다.

| scope | 의미 | 예시 |
|-------|------|------|
| compile (기본) | 컴파일 + 실행 모두 포함 | Spring, MyBatis |
| `provided` | 컴파일엔 쓰지만 WAR에 포함 안 함 | Servlet API, JSP API |
| `test` | 테스트 코드에서만 사용 | JUnit |

Servlet API에 `provided`를 붙이는 이유:  
→ Tomcat 서버 자체에 `servlet-api.jar`가 이미 들어 있기 때문.  
→ WAR에도 넣으면 클래스 충돌(ClassNotFoundException)이 발생한다.

---

## 추가된 의존성 목록

### Spring MVC
```xml
<artifactId>spring-webmvc</artifactId>
```
`@Controller`, `@RequestMapping`, `DispatcherServlet` 등  
Spring MVC의 모든 기능이 여기서 온다.

### MyBatis + mybatis-spring
```xml
<artifactId>mybatis</artifactId>
<artifactId>mybatis-spring</artifactId>
```
SQL을 XML 파일에 작성하고 Java 인터페이스(Mapper)로 호출하는 방식.  
레거시 시스템에서 JPA 대신 MyBatis를 많이 쓰는 이유:  
→ SQL을 직접 제어해야 하는 복잡한 쿼리가 많기 때문.

### commons-dbcp2
```xml
<artifactId>commons-dbcp2</artifactId>
```
DB 커넥션 풀. 매 요청마다 DB 연결을 새로 맺으면 느리기 때문에  
미리 연결을 여러 개 만들어두고 재사용하는 장치.

### JSTL
```xml
<artifactId>jstl</artifactId>
```
JSP에서 `<c:forEach>`, `<c:if>` 같은 태그를 쓸 수 있게 해줌.

### Lombok
```xml
<artifactId>lombok</artifactId>
<scope>provided</scope>
```
`@Getter`, `@Setter`, `@ToString` 등으로 VO(Value Object) 보일러플레이트 제거.  
컴파일 타임에만 필요하므로 `provided`.

---

## 빌드 플러그인

### maven-war-plugin
`mvn package` 실행 시 `.war` 파일을 생성해주는 플러그인.

### tomcat7-maven-plugin
```
mvn tomcat7:run
```
위 명령어 하나로 내장 Tomcat을 띄워 로컬 개발 테스트 가능.  
`http://localhost:8080/mmall` 로 접속.

---

## 정리

```
pom.xml이 하는 일
├── 프로젝트 정보 (groupId, artifactId, version)
├── 빌드 타입 선언 → war
├── 필요한 라이브러리 목록 (dependencies)
│   ├── Spring MVC      → 웹 요청 처리
│   ├── Servlet/JSP API → 서블릿 규약 (provided)
│   ├── JSTL            → JSP 태그
│   ├── MyBatis         → SQL 매핑
│   ├── DBCP2           → DB 커넥션 풀
│   ├── Logback         → 로그 출력
│   └── Lombok          → 코드 간략화
└── 빌드 플러그인
    ├── maven-war-plugin → .war 패키징
    └── tomcat7-plugin   → 로컬 실행
```
