# Runtime stage - assumes JAR is pre-built with 'mvn package'
# To build: cd simple-crud-api && mvn -f server/pom.xml package -DskipTests
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY server/target/crud-api-server-1.0.0-SNAPSHOT.jar ./server.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/server.jar"]
