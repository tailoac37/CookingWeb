
FROM maven:3.6.3-openjdk-8 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package spring-boot:repackage -DskipTests
FROM eclipse-temurin:8-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENV PORT=8080
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-Dserver.port=${PORT}", "-jar", "app.jar"]

