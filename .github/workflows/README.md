# GitHub Actions Workflows

This directory contains the CI/CD workflows for the orderservice (simple-crud-api/server).

## Workflows

### 1. ci-cd.yml (Main Pipeline)
The main CI/CD pipeline that runs on push and pull requests to `main` and `dev` branches.

**Triggers:**
- Push to `main` or `dev` branches
- Pull requests to `main` or `dev` branches

**Jobs:**
- Runs build and test
- Builds and pushes Docker image (only on push to main)
- Generates API documentation (only on push to main)

### 2. build-test.yml (Reusable Workflow)
Builds and tests the Java/Maven application.

**Features:**
- Sets up Java 17 with Maven cache
- Extracts version from pom.xml
- Compiles the application
- Runs tests
- Packages the application
- Uploads build artifacts

### 3. docker.yml (Reusable Workflow)
Builds and pushes Docker images to GitHub Container Registry.

**Features:**
- Builds multi-stage Docker image
- Pushes to ghcr.io
- Tags with version, latest, and commit SHA
- Creates Git tags for releases
- Uses Docker layer caching

### 4. api-docs.yml (Reusable Workflow)
Generates OpenAPI documentation from the running application.

**Features:**
- Builds and starts the application
- Fetches OpenAPI spec from the running service
- Uploads the spec as an artifact

## Project Structure

The workflows are designed for the `simple-crud-api/server` project which:
- Is a Spring Boot 3.2.2 application
- Uses Java 17
- Uses Maven for building
- Generates OpenAPI stubs from `api-spec/openapi.yaml`
- Exposes API documentation at `/v3/api-docs`

## Docker Build

The Dockerfile has been updated to work with GitHub Actions:
- Build context: repository root
- Dockerfile path: `simple-crud-api/server/Dockerfile`
- Multi-stage build that compiles and packages the application

## Usage

These workflows run automatically on push and pull requests. To manually trigger workflows:

```bash
# All workflows run automatically on push
git push origin main

# Pull requests also trigger the build-and-test workflow
```

## Artifacts

The workflows produce the following artifacts:
- `crud-api-server-jar`: The built JAR file
- `openapi-spec`: The OpenAPI specification JSON
- Docker images pushed to `ghcr.io/simonstreuli/modul-321`
