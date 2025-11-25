# base.Dockerfile for main-backend
# Gradle + JDK 21 빌드 환경

FROM gradle:8.5-jdk21 AS base

WORKDIR /app

# Gradle 캐싱을 위한 의존성 먼저 다운로드
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# 의존성 다운로드 (캐싱)
RUN gradle dependencies --no-daemon || true

# 캐시 정리
RUN rm -rf ~/.gradle/caches/*/plugin-resolution/