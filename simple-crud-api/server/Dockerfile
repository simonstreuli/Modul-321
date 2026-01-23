# Runtime stage - assumes JAR is pre-built with 'mvn package'
# To build: docker build -f simple-crud-api/server/Dockerfile -t crud-api-server .
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
# Copy simple-crud-api directory including api-spec for OpenAPI generation
COPY simple-crud-api .
WORKDIR /app/server
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /app/server/target/crud-api-server-1.0.0-SNAPSHOT.jar ./server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/server.jar"]
