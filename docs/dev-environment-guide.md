# 개발 환경 실행 가이드

## 사전 준비 (필수 설치)

| 항목 | 버전 | 확인 명령어 |
|------|------|-------------|
| Java JDK | 11 | `java -version` |
| Maven | 3.6+ | `mvn -version` |
| IntelliJ IDEA | 최신 | — |
| Git | 최신 | `git --version` |

> Tomcat은 별도 설치 불필요 — Maven 플러그인으로 내장 실행

---

## 1. 로컬 실행 (Maven 내장 Tomcat)

### 터미널에서 실행

```bash
# 프로젝트 루트로 이동
cd D:/goods-legacy

# 내장 Tomcat 시작 (포트 8080)
mvn tomcat7:run
```

실행 후 브라우저 접속:
```
http://localhost:8080/mmall/goods/goodsDetail.do?gdCd=2735991
```

### IntelliJ에서 실행 (Run Configuration 등록)

1. 상단 메뉴 `Run → Edit Configurations`
2. 좌상단 `+` → `Maven` 선택
3. 아래와 같이 설정:

```
Name        : tomcat7:run
Working dir : $PROJECT_DIR$
Goals       : tomcat7:run
```

4. `Apply → OK` 후 ▶ 버튼으로 실행

---

## 2. Docker로 실행 (Oracle DB 포함)

개발 DB를 Docker로 띄우면 로컬에 Oracle 설치 없이 바로 사용 가능.

### docker-compose.yml 생성

프로젝트 루트에 `docker-compose.yml` 파일 생성:

```yaml
version: '3.8'

services:

  oracle-db:
    image: gvenzl/oracle-xe:21-slim
    container_name: mmall-oracle
    environment:
      ORACLE_PASSWORD: oracle123       # sys/system 비밀번호
      APP_USER: mmall                  # 앱 전용 유저
      APP_USER_PASSWORD: mmall123
    ports:
      - "1521:1521"
    volumes:
      - oracle-data:/opt/oracle/oradata
      - ./sql/init:/container-entrypoint-initdb.d  # 초기 SQL 자동 실행
    healthcheck:
      test: ["CMD", "healthcheck.sh"]
      interval: 30s
      timeout: 10s
      retries: 5

volumes:
  oracle-data:
```

### Docker 실행 명령어

```bash
# 컨테이너 시작 (처음 실행 시 이미지 다운로드 포함, 수 분 소요)
docker-compose up -d

# 컨테이너 상태 확인
docker-compose ps

# Oracle 로그 확인 (기동 완료 메시지 확인용)
docker-compose logs -f oracle-db

# 컨테이너 중지 (데이터 유지)
docker-compose stop

# 컨테이너 + 데이터 완전 삭제
docker-compose down -v
```

### DB 기동 확인 후 접속 정보

| 항목 | 값 |
|------|-----|
| Host | localhost |
| Port | 1521 |
| SID / Service | XE |
| User | mmall |
| Password | mmall123 |

`applicationContext.xml` DB 설정과 일치해야 한다:

```xml
<property name="url" value="jdbc:oracle:thin:@localhost:1521:XE"/>
<property name="username" value="mmall"/>
<property name="password" value="mmall123"/>
```

---

## 3. 초기 테이블 생성 SQL

`sql/init/01_create_tables.sql` 파일을 만들어두면 Docker 첫 실행 시 자동 실행:

```sql
-- 상품 테이블
CREATE TABLE TB_GOODS (
    GD_CD       VARCHAR2(20)    NOT NULL,
    GD_NM       VARCHAR2(200)   NOT NULL,
    GD_PRICE    NUMBER(10)      DEFAULT 0,
    GD_DESC     CLOB,
    GD_IMG_URL  VARCHAR2(500),
    USE_YN      CHAR(1)         DEFAULT 'Y',
    REG_DT      DATE            DEFAULT SYSDATE,
    CONSTRAINT PK_TB_GOODS PRIMARY KEY (GD_CD)
);

-- 샘플 데이터
INSERT INTO TB_GOODS (GD_CD, GD_NM, GD_PRICE, GD_DESC, USE_YN)
VALUES ('2735991', '테스트 상품', 50000, '상품 설명입니다.', 'Y');

COMMIT;
```

---

## 4. 전체 개발 실행 순서

```
1. Docker로 Oracle DB 시작
   docker-compose up -d

2. DB 기동 확인 (약 1~2분 소요)
   docker-compose logs -f oracle-db
   → "DATABASE IS READY TO USE!" 메시지 확인

3. 앱 실행
   mvn tomcat7:run
   (또는 IntelliJ ▶ 버튼)

4. 브라우저 접속
   http://localhost:8080/mmall/goods/goodsDetail.do?gdCd=2735991
```

---

## 5. 자주 쓰는 Maven 명령어

```bash
# 컴파일만
mvn compile

# 테스트 실행
mvn test

# WAR 파일 생성 (target/*.war)
mvn package

# 테스트 건너뛰고 WAR 생성
mvn package -DskipTests

# 의존성 다운로드 + 컴파일
mvn install

# 빌드 결과물 삭제
mvn clean

# clean + WAR 생성 (가장 많이 쓰는 조합)
mvn clean package
```

---

## 6. 포트 충돌 시 해결

8080 포트가 이미 사용 중일 때:

```bash
# Windows - 8080 포트 사용 중인 PID 확인
netstat -ano | findstr :8080

# 해당 PID 종료
taskkill /PID [PID번호] /F
```

또는 `pom.xml`에서 포트 변경:

```xml
<configuration>
    <port>9090</port>   <!-- 8080 → 9090 -->
    <path>/mmall</path>
</configuration>
```

---

## 7. IntelliJ 자주 쓰는 단축키

| 단축키 | 기능 |
|--------|------|
| `Ctrl + Shift + F` | 전체 파일 내 텍스트 검색 |
| `Ctrl + N` | 클래스 이름으로 파일 열기 |
| `Ctrl + Shift + N` | 파일 이름으로 파일 열기 |
| `Alt + Insert` | Getter/Setter 자동 생성 |
| `Ctrl + Alt + L` | 코드 자동 정렬 |
| `Shift + F10` | 마지막 실행 재실행 |
| `Ctrl + F9` | 빌드 |

---

## 8. 트러블슈팅

| 증상 | 원인 | 해결 |
|------|------|------|
| 한글 깨짐 | 인코딩 필터 누락 | `web.xml` CharacterEncodingFilter 확인 |
| 404 오류 | URL 패턴 불일치 | `@RequestMapping` 경로와 URL 비교 |
| 500 오류 | Bean 주입 실패 | applicationContext.xml 스캔 패키지 확인 |
| DB 연결 실패 | Docker 미기동 or 접속정보 오류 | `docker-compose ps` → applicationContext.xml URL 확인 |
| CSS/JS 404 | 정적 자원 설정 누락 | dispatcher-servlet.xml `<mvc:resources>` 확인 |
| `*.do` 404 | DispatcherServlet 매핑 오류 | `web.xml` url-pattern 확인 |
