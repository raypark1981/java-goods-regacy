# 프로젝트 시작 가이드 (Mac 기준)

## 사전 확인

```bash
brew --version   # Homebrew 설치 여부
java -version    # 11 이상
mvn -version     # 3.6 이상
docker --version # 설치 여부
```

---

## 1단계 — Java 11 설치

```bash
brew install openjdk@11 && \
sudo ln -sfn /opt/homebrew/opt/openjdk@11/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-11.jdk && \
echo 'export JAVA_HOME=/opt/homebrew/opt/openjdk@11' >> ~/.zshrc && \
echo 'export PATH="$JAVA_HOME/bin:$PATH"' >> ~/.zshrc && \
source ~/.zshrc && \
java -version
```

> `sudo` 실행 시 Mac 로그인 비밀번호 입력 필요.

> Homebrew 미설치 시 먼저 설치: https://brew.sh

---

## 2단계 — Maven 설치

```bash
brew install maven
mvn -version
```

---

## 3단계 — Docker Desktop 설치

https://www.docker.com/products/docker-desktop/ 에서 Mac 버전 다운로드 후 설치.
설치 후 Docker Desktop 앱 실행 (메뉴바에 고래 아이콘 뜨면 준비 완료).

---

## 4단계 — Oracle DB 실행 (Docker)

> ⚠️ 이미지 다운로드가 포함되어 있어 **5단계와 한 번에 실행하면 안 됩니다.**
> 아래 두 명령어를 순서대로 따로 실행하세요.

**① 컨테이너 시작 (처음 실행 시 이미지 다운로드 포함, 수 분 소요)**
```bash
docker run -d \
  --name mmall-oracle \
  -e ORACLE_PASSWORD=oracle123 \
  -e APP_USER=mmall \
  -e APP_USER_PASSWORD=mmall123 \
  -p 1521:1521 \
  gvenzl/oracle-xe:21-slim
```

**② 기동 완료 확인 (1~2분 소요)**
```bash
docker logs -f mmall-oracle | grep "DATABASE IS READY"
```

`DATABASE IS READY TO USE!` 메시지 나오면 Ctrl+C로 로그 종료.
그 다음 5단계로 이동.

---

## 5단계 — 앱 실행

```bash
cd /Users/younghwanpark/Documents/java-goods-legacy
mvn cargo:run
```

---

## 6단계 — 브라우저 접속

```
http://localhost:8080/mmall/goods/detail.do?gdCd=2735991
```

---

## 앱 종료

터미널에서 `Ctrl+C`

Oracle DB 컨테이너 중지 (데이터 유지):
```bash
docker stop mmall-oracle
```

다음에 다시 시작할 때:
```bash
docker start mmall-oracle
mvn cargo:run
```

---

## 트러블슈팅

| 증상 | 확인 |
|------|------|
| DB 연결 실패 | `docker ps` 로 컨테이너 실행 중인지 확인 |
| 8080 포트 충돌 | `lsof -i :8080` 으로 점유 프로세스 확인 후 종료 |
| 404 오류 | URL 경로와 `@RequestMapping` 값 비교 |
| 500 오류 | 터미널 스택트레이스 확인 |
