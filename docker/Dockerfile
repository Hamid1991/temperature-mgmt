FROM eclipse-temurin:17-jdk-alpine

COPY /build/libs/*.jar temperature-mgmt-backend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "temperature-mgmt-backend.jar"]