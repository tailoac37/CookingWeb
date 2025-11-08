# Build stage
FROM maven:3.6.3-openjdk-8 AS build
WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn clean package spring-boot:repackage -DskipTests

# Runtime stage
FROM eclipse-temurin:8-jre-jammy
WORKDIR /app

# Copy jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 10000

# Set environment variables
ENV PORT=10000
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run application
ENTRYPOINT exec java $JAVA_OPTS -Dspring.profiles.active=dev -Dserver.port=$PORT -jar app.jar
