FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Build the application
RUN apk add --no-cache maven && \
    mvn clean package -DskipTests && \
    mv target/*.jar app.jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/app.jar .

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
