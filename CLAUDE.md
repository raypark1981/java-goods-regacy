# goods-legacy 프로젝트 컨텍스트

## 프로젝트 개요
현대카드 M포인트몰 레거시 시스템 분석/학습용 프로젝트.
Spring MVC + MyBatis + Oracle 구성. `.do` URL 패턴 사용.

## 기술 스택
- Java 11, Maven, WAR 패키징
- Spring MVC 5.3.x (XML 설정 방식, 어노테이션 X)
- MyBatis 3.5.x + XML Mapper
- Oracle DB (DBCP2 커넥션 풀)
- JSP + JSTL (뷰 레이어)
- 내장 Tomcat: `mvn cargo:run` (cargo-maven3-plugin + Tomcat 9 embedded)

## 디렉토리 구조
```
src/main/
├── java/com/hyundaicard/mmall/
│   ├── controller/   @Controller
│   ├── service/      @Service
│   ├── dao/          @Mapper (인터페이스만, 구현체 없음)
│   └── model/        VO (Lombok @Getter/@Setter)
├── resources/
│   └── mapper/       SQL XML 파일
└── webapp/WEB-INF/
    ├── web.xml                      Tomcat 진입점
    ├── spring/dispatcher-servlet.xml  웹 계층 설정
    ├── spring/applicationContext.xml  DB/MyBatis/트랜잭션
    └── views/                       JSP 파일
```

## 설정 파일 역할
| 파일 | 담당 |
|------|------|
| `web.xml` | DispatcherServlet(*.do), 인코딩 필터 |
| `dispatcher-servlet.xml` | Controller 스캔, ViewResolver, 정적자원 |
| `applicationContext.xml` | DataSource, SqlSessionFactory, 트랜잭션 |

## 실행
```bash
mvn cargo:run         # 로컬 실행 → http://localhost:8080/mmall
mvn clean package     # WAR 빌드
```

## DB 접속 정보 (로컬 Docker)
- URL: `jdbc:oracle:thin:@localhost:1521:XE`
- User: `mmall` / PW: `mmall123`
- Docker 시작: `docker-compose up -d`

## 커밋 규칙
- 메시지는 한글로, 간략한 설명 포함
- 형식: `type: 제목\n\n본문`

## 학습 문서 위치
`docs/` 폴더에 단계별 MD 파일로 정리되어 있음.
