# Runtime stage - assumes JAR is pre-built with 'mvn package'
# To build: cd simple-crud-api && mvn -f client/pom.xml package -DskipTests
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY client/target/crud-api-client-1.0.0-SNAPSHOT.jar ./client.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","/app/client.jar"]
