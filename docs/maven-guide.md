# Maven 학습 가이드

## 1. Maven이란?

Maven은 자바 프로젝트를 **빌드**, **테스트**, **패키징**, **의존성 관리**하는 도구다.

터미널에서 Maven을 실행할 때 쓰는 명령어가 `mvn` 이다.

예:

```bash
mvn compile
mvn test
mvn package
```

---

## 2. `mvn`의 역할

`mvn` 명령은 `pom.xml`을 읽고 프로젝트 작업을 수행한다.

대표 역할:

1. 의존성 다운로드
2. 소스 컴파일
3. 테스트 실행
4. WAR/JAR 생성
5. 로컬 저장소 설치

이 프로젝트에서는 `pom.xml` 기준으로 Spring MVC, MyBatis, Lombok 같은 라이브러리를 내려받고,
최종적으로 WAR 파일을 만든다.

---

## 3. `pom.xml`이란?

`pom.xml`은 Maven 프로젝트의 핵심 설정 파일이다.

여기에 들어가는 내용:

- 프로젝트 이름, 버전
- Java 버전
- 의존성 라이브러리
- 빌드 방식
- 플러그인 설정

즉 `mvn`은 **`pom.xml`에 적힌 규칙대로 동작**한다.

---

## 4. Maven 기본 명령어

### `mvn clean`

이전 빌드 결과물을 삭제한다.

보통 `target/` 폴더가 지워진다.

```bash
mvn clean
```

### `mvn compile`

메인 자바 소스를 컴파일한다.

- `.java` → `.class`
- 테스트는 실행하지 않음
- WAR/JAR도 만들지 않음

```bash
mvn compile
```

### `mvn test`

테스트 코드를 컴파일하고 테스트를 실행한다.

```bash
mvn test
```

### `mvn package`

컴파일과 테스트를 거쳐 최종 결과물을 만든다.

이 프로젝트는 WAR 프로젝트라 `target/*.war` 파일이 생성된다.

```bash
mvn package
```

### `mvn install`

`package`까지 수행한 뒤 결과물을 로컬 Maven 저장소(`~/.m2`)에 설치한다.

다른 로컬 프로젝트에서 재사용할 때 의미가 있다.

```bash
mvn install
```

---

## 5. 가장 자주 쓰는 조합

### `mvn clean package`

실무에서 가장 많이 쓰는 조합이다.

의미:

1. 이전 빌드 결과 삭제
2. 새로 컴파일
3. 테스트
4. WAR 생성

```bash
mvn clean package
```

### `mvn package -DskipTests`

테스트를 건너뛰고 빠르게 WAR만 만들고 싶을 때 사용한다.

```bash
mvn package -DskipTests
```

---

## 6. Maven 기본 흐름

자주 보는 흐름만 단순화하면 아래와 같다.

```text
clean → compile → test → package → install
```

각 단계 의미:

- `clean` : 이전 결과 삭제
- `compile` : 소스 컴파일
- `test` : 테스트 실행
- `package` : 배포 파일 생성
- `install` : 로컬 저장소 등록

---

## 7. 이 프로젝트 기준으로 이해하기

이 프로젝트는:

- Java 11
- Maven
- WAR 패키징
- Spring MVC
- MyBatis

구조이므로 Maven은 주로 아래 용도로 쓴다.

### 빌드

```bash
mvn clean package
```

### 테스트

```bash
mvn test
```

### 실행용 패키지 생성

```bash
mvn package
```

생성 결과:

```text
target/java-goods-legacy.war
```

---

## 8. 정리

- `mvn` = Maven 실행 명령어
- `pom.xml` = Maven 설정 파일
- `compile` = 컴파일만
- `test` = 테스트 실행
- `package` = WAR/JAR 생성
- `install` = 로컬 저장소 설치
- 이 프로젝트에서 가장 많이 쓰는 명령은 `mvn clean package`
