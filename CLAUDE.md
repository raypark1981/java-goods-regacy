# goods-legacy 프로젝트 컨텍스트

## 프로젝트 개요
현대백화점 통합멤버십(H.Point) 레거시 시스템 분석/학습용 프로젝트.
실제 운영 프로젝트(`hmfrnt`)와 동일한 구조로 재현한 골격 프로젝트.
Spring MVC + MyBatis + Oracle 구성. URL 패턴: `.nhd` / `.shd` / `.hd`

## 기술 스택
- Java 11, Maven, WAR 패키징
- Spring MVC 5.3.x (XML 설정 방식)
- MyBatis 3.5.x + XML Mapper
- Oracle DB (DBCP2 커넥션 풀)
- JSP + JSTL (뷰 레이어)
- 내장 Tomcat: `mvn cargo:run` (cargo-maven3-plugin + Tomcat 9 embedded)

## 디렉토리 구조
```
src/main/
├── java/hmfrnt/
│   ├── web/          BaseController, BaseObject, ApiResponseVO (공통 베이스)
│   ├── common/       HdgmMap, HdgmUtil, XssUtil, HdgmAbstractService
│   ├── config/       OpenApiConfig 등 설정 클래스
│   ├── ap/           승인 업무 (web/service/service.impl/mapper/vo/dto)
│   ├── api/          외부 API
│   ├── cc/ccb/       결제
│   ├── cm/           공통 (cma/cmb/cme/cmf/cmg/cmn/cmo/cmp/cmr)
│   └── cu/           화면 (cua/cub/cuc/cud)
├── resources/
│   └── mapper/{업무}/  SQL XML 파일
└── webapp/WEB-INF/
    ├── web.xml
    ├── spring/dispatcher-servlet.xml
    ├── spring/applicationContext.xml
    └── jsp/{업무}/   JSP 파일
```

## 설정 파일 역할
| 파일 | 담당 |
|------|------|
| `web.xml` | DispatcherServlet (`*.nhd`, `*.hd`, `*.shd`), 인코딩 필터 |
| `dispatcher-servlet.xml` | Controller 스캔(`hmfrnt`), ViewResolver(`/WEB-INF/jsp/`) |
| `applicationContext.xml` | DataSource, SqlSessionFactory, 트랜잭션, Mapper 스캔 |

## 실행
```bash
mvn cargo:run         # 로컬 실행 → http://localhost:8080/mmall
mvn clean package     # WAR 빌드
```

## DB 접속 정보 (로컬 Docker)
- URL: `jdbc:oracle:thin:@//localhost:1521/XEPDB1`
- User: `mmall` / PW: `mmall123`
- Docker 시작: `docker-compose up -d`

---

## 통합멤버십 개발표준 (코드 작성 시 반드시 준수)

> 상세 내용: `docs/project/통합멤버십-개발표준안.md`

### 네이밍
- 클래스: `PascalCase` / 메서드·변수: `camelCase` / 상수·DB컬럼: `UPPER_SNAKE_CASE`
- 파일 접미어: `Controller`, `ServiceImpl`, `DAO` 고정
- 패키지: `{프로젝트명}.{업무대분류}.{업무중분류}.{레이어}`
  - 예) `hmfrnt.ap.apa.web`, `hmfrnt.cu.cud.service.impl`

### 업무 코드
| 코드 | 업무 |
|------|------|
| AP | 승인 |
| CC | 결제 |
| PE | 선불충전 |
| CU | 화면 |
| CP | 관리자 |
| CM | 공통 |

### URI 패턴
| 확장자 | 대상 |
|--------|------|
| `*.hd` | 일반 API |
| `*.nhd` | 비로그인 고객 API |
| `*.shd` | 로그인 고객 API |

### Java 코드 규칙
- 인덴트: **4 spaces**
- `System.out` 사용 금지 → `logger` 사용
- 민감정보(주민번호·카드번호·토큰·패스워드) 로그 출력 금지
- 트랜잭션: **Service 단위만** 사용, Controller에서 사용 금지

### SQL 규칙
- queryId 형식: `DAO명.쿼리명`
- 쿼리 첫줄에 queryId 주석 기입
- 신규 쿼리는 ANSI JOIN으로 작성
- 쿼리는 MyBatis Mapper XML로 통일 (애노테이션 SQL 금지)

### 로그 레벨
- `DEBUG`: 메서드 시작/끝
- `INFO`: request/response, 외부 연동, 쿼리 바인딩
- `WARN`: 재처리 성공, 임계치(3~5초) 초과, deprecated 호출
- `ERROR`: Exception, 트랜잭션 롤백, 시스템 장애 (stack trace + URL/method/status 포함)

### API 에러 응답 필수 필드
```json
{ "code": "응답코드", "message": "응답메시지" }
```

---

## Git / 브랜치 규칙
- `master`: 운영 서버
- `develop`: 개발 서버
- `stg`: 스테이징 서버
- `feature/*`: 로컬 개발 브랜치
- 환경파일 분리: `.dev` / `.stg` / `*.prd`

## 커밋 규칙
- 메시지는 한글로, 간략한 설명 포함
- 형식: `type: 제목\n\n본문`

## 학습/참고 문서
- `docs/project/통합멤버십-개발표준안.md` — 전체 개발 표준
- `docs/project/포인트충전-전환-요구사항.md` — SFR 요구사항 및 주차별 매핑
- `docs/project/API-개발일정.md` — 박영환 담당 API 개발 일정
- `docs/project/자동충전-금액-노출기준.md` — 자동충전 금액 산정 정책

## 추가 해석 규칙
- 사용자가 "이미 개발되어 있는 것 같다"라고 말하면, 외부 공개 코드 탐색보다 **현재 프로젝트와 연결된 회사 내부망/기존 내부 소스 문맥**으로 우선 해석한다.
- 이 경우 사용자가 별도 요청하지 않으면 저장소 전체 소스 검색으로 단정하지 말고, 먼저 내부 선행 구현이 있다고 가정하고 설명한다.

## MCP 확인 규칙
- 사용자가 "토스 MCP 연결 확인"을 요청하면 `.mcp.json` 설정 파일은 이미 있다고 전제하고, 설정 파일을 반복 확인하지 않는다.
- 확인은 `@tosspayments/integration-guide-mcp` 서버의 실제 연결/초기화 성공 여부만 검증한다.
