# out/ 폴더란?

IntelliJ가 프로젝트를 빌드할 때 생성하는 **빌드 산출물 폴더**다.
`src/` 소스를 컴파일·복사한 결과물이 담기며, git에 올리지 않는다.

---

## 구조와 각 폴더 역할

```
out/artifacts/goods_legacy/
├── WEB-INF/
│   ├── classes/    ← .java 컴파일 결과 (.class 파일)
│   ├── lib/        ← Maven 의존성 jar 복사본
│   ├── spring/     ← applicationContext.xml, dispatcher-servlet.xml 복사본
│   ├── views/      ← JSP 파일 복사본
│   └── web.xml     ← web.xml 복사본
```

### classes/
- `.java` 소스를 컴파일한 바이트코드 (.class)
- .NET의 `bin/Debug/` 안 `.dll`과 같은 개념
- JVM이 실제로 실행하는 파일

### lib/
- `pom.xml`에 선언한 의존성 jar 파일 묶음
- .NET NuGet 패키지가 `bin/`에 복사되는 것과 동일
- jar는 설치파일이 아니라 **남이 만들어둔 .class 묶음**
- Tomcat 시작 시 클래스 로더가 메모리에 올림

### spring/
- `src/main/webapp/WEB-INF/spring/` 파일을 **그대로 복사**
- 컴파일 없이 복사만 하기 때문에 소스 수정 후 재빌드 전까지 이전 내용 유지

### views/
- JSP 파일 복사본
- 화면 출력용 템플릿 (.NET Razor `.cshtml`과 동일 역할)

---

## Maven target/ vs IntelliJ out/ 차이

| | `target/` | `out/` |
|---|---|---|
| 생성 주체 | `mvn package` / `mvn cargo:run` | IntelliJ Build |
| 사용 주체 | Maven, cargo 플러그인 | IntelliJ Tomcat 실행 |
| 서로 공유? | X (독립적) | X (독립적) |

- `mvn cargo:run` → `target/` 기준으로 실행
- IntelliJ Tomcat 실행 → `out/` 기준으로 실행
- 소스 수정 후 반영하려면 각각 다시 빌드 필요

---

## git에서 제외하는 이유

- 빌드할 때마다 자동 생성되므로 git으로 관리할 필요 없음
- `.class`, `.jar` 같은 바이너리 파일은 diff가 의미 없음
- `target/`과 동일한 이유로 `.gitignore`에 추가
