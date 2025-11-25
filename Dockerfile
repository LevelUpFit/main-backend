# Dockerfile for main-backend
# Spring Boot + JDK 21 애플리케이션

# 빌드 스테이지
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app

# Gradle 설정 파일 복사
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 소스 코드 복사
COPY src ./src

# Gradle 빌드 (테스트 제외)
RUN gradle clean build -x test --no-daemon

# 런타임 스테이지
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# 타임존 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# curl 설치 (헬스체크용)
RUN apt-get update && \
    apt-get install -y curl && \
    rm -rf /var/lib/apt/lists/*

# 빌드된 JAR 파일 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 포트 노출
EXPOSE 8080

# 헬스체크
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# JVM 옵션 설정
ENV JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# Spring Boot 실행
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]