# KN_02: Simple CRUD API - Contract First Approach

## Overview

This project demonstrates a **Contract-First API development** approach using OpenAPI specification. The API contract is defined in `api-spec/openapi.yaml`, and both server stubs (interfaces & DTOs) and client stubs (classes & DTOs) are automatically generated from this contract.

## Project Structure

```
simple-crud-api/
├── api-spec/
│   └── openapi.yaml          # OpenAPI 3.0 specification (the contract)
├── server/
│   ├── pom.xml               # Maven config with openapi-generator for server stubs
│   ├── Dockerfile
│   └── src/main/java/
│       └── com/example/server/
│           ├── ServerApplication.java
│           └── controller/
│               └── ItemsController.java    # Implements generated ItemsApi interface
├── client/
│   ├── pom.xml               # Maven config with openapi-generator for client stubs
│   ├── Dockerfile
│   └── src/main/java/
│       └── com/example/client/
│           ├── ClientApplication.java
│           ├── config/
│           │   └── ApiClientConfig.java    # Configures generated ItemsApi client
│           └── runner/
│               └── ApiClientRunner.java    # Demonstrates API usage
├── docker-compose.yml
└── README.md
```

## Features

- **Contract First**: OpenAPI specification defines the API contract
- **Generated Server Stubs**: Interfaces and DTOs generated from OpenAPI spec
- **Generated Client Stubs**: Client classes and DTOs generated from OpenAPI spec
- **Spring Boot 3.2**: Modern Java framework
- **Docker Compose**: Easy deployment and testing

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET    | /api/items | Get all items |
| GET    | /api/items/{id} | Get item by ID |
| POST   | /api/items | Create new item |
| PUT    | /api/items/{id} | Update item |
| DELETE | /api/items/{id} | Delete item |

## Building and Running

### Prerequisites

- Java 17+
- Maven 3.9+
- Docker & Docker Compose

### Build with Maven

```bash
# Navigate to simple-crud-api directory
cd simple-crud-api

# Generate stubs and build server
mvn -f server/pom.xml clean package -DskipTests

# Generate stubs and build client
mvn -f client/pom.xml clean package -DskipTests
```

### Run with Docker Compose

**Note:** You must build the JARs with Maven first before running Docker Compose.

```bash
cd simple-crud-api
docker-compose up --build
```

This starts:
- **Server** at http://localhost:8080
- **Client** at http://localhost:8081

The client will automatically call all API endpoints and log the results.

### Access Swagger UI

Once the server is running, access the Swagger UI at:
http://localhost:8080/swagger-ui.html

## Generated Code

The OpenAPI Generator Maven plugin generates:

### Server (spring generator)
- `com.example.server.api.ItemsApi` - Interface with all endpoint methods
- `com.example.server.model.*` - DTOs (Item, CreateItemRequest, UpdateItemRequest)

### Client (java generator with resttemplate)
- `com.example.client.api.ItemsApi` - Client class for making API calls
- `com.example.client.model.*` - DTOs (Item, CreateItemRequest, UpdateItemRequest)
- `com.example.client.ApiClient` - HTTP client configuration

## Technologies

- OpenAPI 3.0
- Spring Boot 3.2.2
- OpenAPI Generator Maven Plugin 7.2.0
- Docker & Docker Compose
