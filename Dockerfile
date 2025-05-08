# Step 1: Gradle 빌드 단계
FROM gradle:8.5-jdk17 AS build

WORKDIR /app

# 캐시 최적화용: 의존성 먼저 복사
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
RUN gradle dependencies || true

# 전체 복사 후 빌드
COPY . .
RUN gradle clean build -x test

# Step 2: 실행 단계
FROM openjdk:17-jdk-slim

WORKDIR /app

# JAR 복사 (1개만 존재한다는 가정)
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
