FROM amazoncorretto:17

ENV SERVER_PORT=9100

EXPOSE ${SERVER_PORT}

WORKDIR /huii/extend

ADD ./target/huii-springboot-admin.jar ./huii-springboot-admin.jar

ENTRYPOINT ["java", "-Dserver.port=${SERVER_PORT}", "-jar", "huii-springboot-admin.jar"]
