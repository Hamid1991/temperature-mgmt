FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY build/libs/*.jar /app/temperature-mgmt-backend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "temperature-mgmt-backend.jar"]