FROM openjdk:17-jdk-slim

MAINTAINER huii

ENV SERVER_PORT=8080

EXPOSE ${SERVER_PORT}

VOLUME /huii

WORKDIR /huii

RUN mkdir -p /huii

COPY ./target/huii-application.jar /huii/huii-application.jar

ENTRYPOINT ["nohup","java","-Dserver.port=${SERVER_PORT}","-jar","huii-application.jar","&"]