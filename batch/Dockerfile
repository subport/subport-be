FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Seoul

WORKDIR /app

COPY api/build/libs/batch.jar batch.jar

ENTRYPOINT ["java", "-jar", "batch.jar"]