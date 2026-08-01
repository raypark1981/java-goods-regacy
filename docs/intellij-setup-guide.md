# IntelliJ 신규 환경 설정 주의점

새 회사 입사 또는 PC 교체 시 IntelliJ 처음 설정할 때 체크리스트.

---

## 1. Maven 프로젝트 인식 확인

IntelliJ가 Maven 프로젝트로 인식했는지 먼저 확인한다.

```
우측 Maven 패널이 보여야 함
없으면 → pom.xml 우클릭 > Add as Maven Project
```

인식 안 되면 의존성 전체가 빨간 줄로 표시된다.

---

## 2. JDK 버전 맞추기

프로젝트마다 Java 버전이 다르다. 버전 불일치 시 컴파일 자체가 안 된다.

```
File > Project Structure > SDK
→ 프로젝트에서 쓰는 버전과 일치해야 함
→ 이 프로젝트는 Java 11
```

---

## 3. 아티팩트 출력 경로 확인

IntelliJ 기본 아티팩트 출력 경로는 `out/`이다.
Maven 의존성은 `target/`에 빌드되므로, `out/`으로 두면 나중에 추가한 jar가 누락된다.
반드시 `target/`으로 맞춰야 Maven과 IntelliJ가 같은 폴더를 공유한다.

```
File > Project Structure > Artifacts
→ 출력 경로가 target/{프로젝트명}-{버전} 인지 확인
→ out/ 이면 target/ 으로 변경
→ 이 프로젝트: target/goods-legacy-1.0-SNAPSHOT
```

pom.xml에 새 의존성 추가 후 아티팩트 lib 목록에도 추가해야 IntelliJ Tomcat에 반영된다.

---

## 4. Tomcat 컨텍스트 경로 확인

컨텍스트 경로가 틀리면 URL이 달라져 404가 뜬다.

```
Run > Edit Configurations > Tomcat > Deployment 탭
→ Application context = /mmall 인지 확인
```

---

## 5. DB 접속 정보 변경

회사 환경마다 로컬 DB 접속 정보가 다르다. 팀에서 주는 로컬 설정값으로 변경한다.

```
src/main/webapp/WEB-INF/spring/applicationContext.xml
→ url / username / password 를 로컬 환경에 맞게 수정
```

Spring Boot라면 `src/main/resources/application.properties` 수정.

---

## 자주 겪는 실수 요약

| 증상 | 원인 | 해결 |
|---|---|---|
| 의존성 전체 빨간 줄 | Maven 프로젝트 미인식 | pom.xml 우클릭 > Add as Maven Project |
| 빌드 에러 | JDK 버전 불일치 | Project Structure > SDK 버전 확인 |
| 500 에러 (드라이버/클래스 없음) | 아티팩트에 jar 누락 | 출력 경로 target/으로 변경 후 Rebuild |
| 404 에러 | 컨텍스트 경로 틀림 | Deployment 탭 Application context 확인 |
| DB 연결 에러 | 접속 정보 미변경 | applicationContext.xml URL/계정 수정 |
