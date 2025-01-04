FROM gradle:8.5-jdk17-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle /app/
COPY src /app/src

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=build /app/build/libs/*.jar /app/temperature-mgmt-backend.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "temperature-mgmt-backend.jar"]