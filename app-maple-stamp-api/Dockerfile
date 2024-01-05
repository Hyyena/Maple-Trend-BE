FROM eclipse-temurin:19
ENV LC_ALL=C.UTF-8

RUN apt-get update && apt-get install -y locales
RUN locale-gen ko_KR.UTF-8
ENV LC_ALL ko_KR.UTF-8

RUN apt-get update && apt-get install -y locales
RUN locale-gen ko_KR.UTF-8
ENV LC_ALL ko_KR.UTF-8

# 컨테이너 내부의 /app 디렉토리에 jar 파일 복사
ARG JAR_FILE=build/libs/app-maple-stamp-api-0.0.1-SNAPSHOT.jar
ARG CONTAINER_JAR_FILE=app-maple-stamp-api.jar
COPY ${JAR_FILE} ${CONTAINER_JAR_FILE}

RUN apt-get update && apt-get -y upgrade && apt-get install -y fonts-nanum* && fc-cache -fv

EXPOSE 9000

# Spring Boot를 실행하기 위한 entry point 지정
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "/app-maple-stamp-api.jar"]

# 시간 설정
ENV TZ=Asia/Seoul