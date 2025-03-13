# 1. JDK 17을 사용하여 빌드
FROM openjdk:17-slim AS build

# curl만 설치
RUN apt-get update && apt-get install -y curl

WORKDIR /app

# Gradle Wrapper와 소스 코드 복사
COPY gradlew gradlew.bat settings.gradle build.gradle ./
COPY gradle ./gradle
COPY src ./src

# 빌드 실행 (프로젝트에 맞는 빌드 명령어 사용)
RUN ./gradlew build -x test

# 2. 빌드한 JAR 파일을 실제 애플리케이션을 실행할 이미지로 복사
FROM openjdk:17-slim

WORKDIR /app

# 빌드한 JAR 파일을 복사
COPY --from=build /app/build/libs/scvgg-0.0.1-SNAPSHOT.jar scvgg.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "scvgg.jar"]

EXPOSE 8080
