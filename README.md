# LevelUpFit Backend

LevelUpFit은 AI 자세 분석과 운동 루틴 관리를 제공하는 스마트 피트니스 플랫폼입니다.  
본 리포지토리는 사용자 관리, 운동 루틴, 운동 기록 등을 처리하는 **Spring Boot 기반 백엔드 서비스**입니다.

## 🧩 주요 기능

- 사용자 회원가입 / 로그인 (폼 로그인 + 카카오 소셜 로그인)
- 운동 종목 관리 (생성, 조회, 수정, 삭제)
- 루틴 생성 및 루틴-운동 매핑
- 루틴 수행 및 운동 기록 저장
- 사용자별 운동 이력 조회
- JWT 기반 인증 / 인가 처리

## 🚀 실행 방법

```bash
# JDK 17 이상 설치 필요
./gradlew build
java -jar build/libs/levelupfit-backend.jar
