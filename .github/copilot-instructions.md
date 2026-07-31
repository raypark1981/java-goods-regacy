# goods-legacy Copilot Instructions

이 문서는 `CLAUDE.md`를 축약한 코파일럿용 지침이다. 반복 설명을 줄이고, 필요한 파일만 읽어 토큰을 아끼는 것을 우선한다.

## 프로젝트 핵심
- 현대카드 M포인트몰 레거시 학습/분석 프로젝트
- Java 11, Maven, WAR
- Spring MVC 5.3.x, XML 설정 방식
- MyBatis 3.5.x + XML Mapper
- Oracle DB, JSP + JSTL
- URL 패턴은 `*.do`

## 주요 경로
- `src/main/java/com/hyundaicard/mmall/controller`
- `src/main/java/com/hyundaicard/mmall/service`
- `src/main/java/com/hyundaicard/mmall/dao`
- `src/main/java/com/hyundaicard/mmall/model`
- `src/main/resources/mapper`
- `src/main/webapp/WEB-INF/views`
- `src/main/webapp/WEB-INF/spring`

## 설정 파일 역할
- `web.xml`: DispatcherServlet(`*.do`), 인코딩 필터
- `dispatcher-servlet.xml`: Controller 스캔, ViewResolver, 정적 자원
- `applicationContext.xml`: DataSource, MyBatis, 트랜잭션

## 실행
- 로컬 실행: `mvn cargo:run`
- 패키징: `mvn clean package`
- DB 기동: `docker-compose up -d`
- 기본 URL: `http://localhost:8080/mmall`

## 작업 규칙
1. 답변은 한국어로 짧고 바로 결론부터 쓴다.
2. 기존 패턴을 먼저 재사용한다. 새 프레임워크, 어노테이션 기반 대개편, 구조 변경은 요청 없으면 하지 않는다.
3. 조사 순서는 관련 파일만 최소 범위로 본다: `Controller -> Service -> DAO -> Mapper XML -> JSP`.
4. `.do` URL, JSP 뷰, MyBatis XML SQL, Oracle 전제를 유지한다.
5. SQL은 `resources/mapper`의 기존 네이밍/구조를 따른다.
6. 검증은 변경 범위에 필요한 최소 명령만 실행한다.
7. 문서가 필요하면 `docs/`를 먼저 참고하고, 불필요한 새 문서는 만들지 않는다.
8. 커밋이 필요하면 한글 메시지 형식 `type: 제목`을 따른다.

## 토큰 절약 규칙
- 이미 위 정보로 충분하면 `CLAUDE.md` 전체를 다시 읽지 않는다.
- 여러 파일이 필요할 때만 병렬로 읽고, 무관한 파일 탐색은 피한다.
- 설명보다 수정과 결과를 우선한다.
