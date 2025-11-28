# Runtime stage - assumes JAR is pre-built with 'mvn package'
# To build: cd simple-crud-api && mvn -f server/pom.xml package -DskipTests
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy everything including api-spec for OpenAPI generation
COPY ../.. .
WORKDIR /app/server
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/server/target/crud-api-server-1.0.0-SNAPSHOT.jar ./server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/server.jar"]
