FROM eclipse-temurin:19 as builder

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootjar

# jar 실행
FROM eclipse-temurin:19 as runtime

USER worker:worker

# 컨테이너 내부의 /app 디렉토리에 jar 파일 복사
#ARG JAR_FILE=app-maple-stamp-api/build/libs/app-maple-stamp-api-0.0.1-SNAPSHOT.jar
#ARG CONTAINER_JAR_FILE=app-maple-stamp-api.jar
#COPY ${JAR_FILE} ${CONTAINER_JAR_FILE}

COPY --from=builder build/libs/*.jar app.jar

EXPOSE 9000

# Spring Boot를 실행하기 위한 entry point 지정
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app.jar"]

# 시간 설정
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata