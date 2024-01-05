FROM eclipse-temurin:19

# 컨테이너 내부의 /app 디렉토리에 jar 파일 복사
ARG JAR_FILE=build/libs/app-maple-stamp-api-0.0.1-SNAPSHOT.jar
ARG CONTAINER_JAR_FILE=app-maple-stamp-api.jar
COPY ${JAR_FILE} ${CONTAINER_JAR_FILE}

# Install Fonts
RUN DEBIAN_FRONTEND=noninteractive apt-get update && apt-get install -y --no-install-recommends fontconfig fonts-liberation

EXPOSE 9000

# Spring Boot를 실행하기 위한 entry point 지정
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app-maple-stamp-api.jar"]

# 시간 설정
ENV TZ=Asia/Seoul