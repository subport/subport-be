FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Seoul

WORKDIR /app

COPY build/libs/api.jar api.jar

ENTRYPOINT ["java", "-jar", "api.jar"]