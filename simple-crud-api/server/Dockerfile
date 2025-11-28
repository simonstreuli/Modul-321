FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY api-spec ../api-spec
COPY server/pom.xml ./
COPY server/src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /workspace/target/crud-api-server-1.0.0-SNAPSHOT.jar ./server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/server.jar"]
