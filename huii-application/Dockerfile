#此镜像目前会导致bug
#Handler dispatch failed: java.lang.NoClassDefFoundError: Could not initialize class sun.font.SunFontManager
#FROM openjdk:17-jdk-slim
#FROM openjdk:17-alpine
#使用amazon镜像
FROM amazoncorretto:17

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

WORKDIR /huii

RUN mkdir -p /huii/logs/ /huii/data/

COPY ./target/huii-application.jar /huii/huii-application.jar

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-jar", "huii-application.jar"]
