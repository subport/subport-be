FROM eclipse-temurin:17-jre-alpine

ENV TZ=Asia/Seoul

WORKDIR /app

COPY admin/build/libs/admin.jar admin.jar

ENTRYPOINT ["java", "-jar", "admin.jar"]