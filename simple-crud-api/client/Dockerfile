FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /workspace
COPY api-spec ../api-spec
COPY client/pom.xml ./
COPY client/src ./src
RUN mvn -B -DskipTests package

FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=build /workspace/target/crud-api-client-1.0.0-SNAPSHOT.jar ./client.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/client.jar"]
