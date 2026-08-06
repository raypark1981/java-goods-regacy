# goods-legacy Copilot Instructions

이 문서는 `CLAUDE.md`를 축약한 코파일럿용 지침이다. 반복 설명을 줄이고, 필요한 파일만 읽어 토큰을 아끼는 것을 우선한다.

## 프로젝트 핵심
- 현대백화점 통합멤버십(H.Point) 레거시 학습/분석 프로젝트
- Java 11, Maven, WAR
- Spring MVC 5.3.x, XML 설정 방식
- MyBatis 3.5.x + XML Mapper
- Oracle DB, JSP + JSTL
- URL 패턴: `*.nhd`(비로그인) / `*.shd`(로그인) / `*.hd`(일반)

## 주요 경로
- `src/main/java/hmfrnt/web/` — BaseController, BaseObject, ApiResponseVO
- `src/main/java/hmfrnt/common/` — HdgmMap, HdgmUtil, XssUtil, HdgmAbstractService
- `src/main/java/hmfrnt/config/` — OpenApiConfig 등
- `src/main/java/hmfrnt/{업무}/web/` — Controller
- `src/main/java/hmfrnt/{업무}/service/` — Service 인터페이스
- `src/main/java/hmfrnt/{업무}/service/impl/` — ServiceImpl
- `src/main/java/hmfrnt/{업무}/mapper/` — DAO (@Mapper)
- `src/main/java/hmfrnt/{업무}/vo/` — VO (BaseObject 상속)
- `src/main/resources/mapper/{업무}/` — SQL XML
- `src/main/webapp/WEB-INF/jsp/{업무}/` — JSP
- `src/main/webapp/WEB-INF/spring/` — Spring 설정

## 업무 코드
AP(승인) / CC(결제) / PE(선불충전) / CU(화면) / CP(관리자) / CM(공통)

## 설정 파일 역할
- `web.xml`: DispatcherServlet(`*.nhd`, `*.shd`, `*.hd`), 인코딩 필터
- `dispatcher-servlet.xml`: Controller 스캔(`hmfrnt`), ViewResolver(`/WEB-INF/jsp/`)
- `applicationContext.xml`: DataSource, MyBatis, 트랜잭션, Mapper 스캔(`hmfrnt`)

## 실행
- 로컬 실행: `mvn cargo:run`
- 패키징: `mvn clean package`
- DB 기동: `docker-compose up -d`
- 기본 URL: `http://localhost:8080/mmall`

## 통합멤버십 개발표준 (코드 작성 시 준수)

### 네이밍
- 클래스: `PascalCase` / 메서드·변수: `camelCase` / 상수·DB컬럼: `UPPER_SNAKE_CASE`
- 파일 접미어 고정: `Controller` / `ServiceImpl` / `DAO`
- 패키지: `hmfrnt.{업무대분류}.{업무중분류}.{레이어}`

### Java
- 인덴트: 4 spaces
- `System.out` 금지 → `logger` 사용
- 민감정보(주민번호·카드번호·토큰·패스워드) 로그 출력 금지
- 트랜잭션: Service 단위만, Controller 사용 금지
- 새 클래스는 반드시 `BaseController` / `HdgmAbstractService` / `BaseObject` 상속

### SQL
- queryId: `DAO명.쿼리명` 형식, 쿼리 첫줄에 주석 기입
- 신규 쿼리: ANSI JOIN 사용
- MyBatis Mapper XML 통일 (애노테이션 SQL 금지)
- 파라미터/결과 타입: `HdgmMap` 또는 전용 VO

### 로그 레벨
- DEBUG: 메서드 시작/끝
- INFO: request/response, 외부 연동, 쿼리 바인딩
- WARN: 재처리 성공, 임계치(3~5초) 초과
- ERROR: Exception, 롤백, 장애 — stack trace + URL/method/status 포함

### API 에러 응답
```json
{ "code": "응답코드", "message": "응답메시지" }
```

## 작업 규칙
1. 답변은 한국어로 짧고 바로 결론부터 쓴다.
2. 기존 패턴 우선 재사용. 새 프레임워크·구조 변경은 요청 없으면 하지 않는다.
3. 조사 순서: `Controller → Service → ServiceImpl → DAO → Mapper XML → JSP`
4. SQL은 `resources/mapper/{업무}/` 기존 네이밍/구조를 따른다.
5. 검증은 변경 범위에 필요한 최소 명령만 실행한다.
6. 문서가 필요하면 `docs/project/`를 먼저 참고하고, 불필요한 새 문서는 만들지 않는다.
7. 커밋이 필요하면 한글 메시지 형식 `type: 제목`을 따른다.

## 참고 문서
- `docs/project/통합멤버십-개발표준안.md` — 전체 개발 표준 상세
- `docs/project/포인트충전-전환-요구사항.md` — SFR 요구사항
- `docs/project/API-개발일정.md` — 개발 일정
- `docs/project/자동충전-금액-노출기준.md` — 자동충전 정책

## 토큰 절약 규칙
- 이미 위 정보로 충분하면 `CLAUDE.md` 전체를 다시 읽지 않는다.
- 여러 파일이 필요할 때만 병렬로 읽고, 무관한 파일 탐색은 피한다.
- 설명보다 수정과 결과를 우선한다.
