# KN_04: IAM with Keycloak - OpenID Connect Integration

This project demonstrates Identity and Access Management (IAM) using Keycloak and OpenID Connect (OIDC). It implements a complete authentication flow using the OAuth 2.0 Authorization Code Flow.

## Overview

This application showcases:
- **Public Page**: Accessible without authentication
- **Protected Page**: Requires login via Keycloak
- **User Information Display**: Extracts and displays data from the ID Token (username, email, etc.)
- **OpenID Connect Code Flow**: Secure authentication using OAuth 2.0/OIDC standards

## Architecture

```
┌─────────────┐          ┌──────────────┐          ┌─────────────┐
│   Browser   │  ◄──────►│ Spring Boot  │  ◄──────►│  Keycloak   │
│             │          │ Application  │          │   (IdP)     │
└─────────────┘          └──────────────┘          └─────────────┘
     User                OAuth2 Client           Identity Provider
```

### Security Principles (CIA Triad)

1. **Confidentiality**: Authentication via OAuth2/OIDC ensures secure user verification
2. **Integrity**: JWT tokens are cryptographically signed by Keycloak
3. **Availability**: Centralized identity management with Keycloak

## Prerequisites

- Docker and Docker Compose
- Java 17+ (for local development)
- Maven 3.6+ (for local development)

## Quick Start

### 1. Start the Services

```bash
cd KN_04_Keycloak
docker compose up --build
```

This will:
- Start Keycloak on port 8081
- Start the Spring Boot application on port 8080

### 2. Configure Keycloak

After Keycloak starts (wait about 60 seconds), configure it:

1. **Access Keycloak Admin Console**
   - URL: http://localhost:8081
   - Username: `admin`
   - Password: `admin`

2. **Create a Realm**
   - Click on the dropdown in the top-left (says "master")
   - Click "Create Realm"
   - Name: `demo-realm`
   - Click "Create"

3. **Create a Client**
   - Go to "Clients" in the left menu
   - Click "Create client"
   - Client ID: `demo-client`
   - Client Type: `OpenID Connect`
   - Click "Next"
   - Client authentication: `ON`
   - Authentication flow: Check "Standard flow" and "Direct access grants"
   - Click "Next"
   - Valid redirect URIs: `http://localhost:8080/*`
   - Valid post logout redirect URIs: `http://localhost:8080/*`
   - Web origins: `http://localhost:8080`
   - Click "Save"

4. **Get Client Secret**
   - In the client settings, go to "Credentials" tab
   - Copy the "Client secret"
   - Update `src/main/resources/application.properties`:
     ```properties
     spring.security.oauth2.client.registration.keycloak.client-secret=<your-client-secret>
     ```

5. **Create a User**
   - Go to "Users" in the left menu
   - Click "Add user"
   - Username: `testuser`
   - Email: `testuser@example.com`
   - First name: `Test`
   - Last name: `User`
   - Email verified: `ON`
   - Click "Create"
   - Go to "Credentials" tab
   - Click "Set password"
   - Password: `password`
   - Temporary: `OFF`
   - Click "Save"

### 3. Restart the Application

After configuring Keycloak and updating the client secret:

```bash
docker compose restart app
```

Or rebuild if you changed application.properties:

```bash
docker compose up --build app
```

### 4. Access the Application

Open your browser and navigate to:
- Application: http://localhost:8080

#### Testing the Flow

1. **Home Page** (http://localhost:8080)
   - Shows welcome message
   - Displays login status

2. **Public Page** (http://localhost:8080/public)
   - Accessible without login
   - Contains public information

3. **Login**
   - Click "Login" button
   - You'll be redirected to Keycloak
   - Enter credentials:
     - Username: `testuser`
     - Password: `password`
   - After successful login, you'll be redirected back to the app

4. **Protected Page** (http://localhost:8080/protected)
   - Only accessible after login
   - Displays user information extracted from ID Token:
     - Username
     - Email
     - Full name
   - Shows all JWT claims
   - Displays raw ID Token

## Project Structure

```
KN_04_Keycloak/
├── src/
│   └── main/
│       ├── java/
│       │   └── ch/modul321/keycloak/
│       │       ├── KeycloakOidcDemoApplication.java
│       │       ├── config/
│       │       │   └── SecurityConfig.java
│       │       └── controller/
│       │           └── HomeController.java
│       └── resources/
│           ├── static/
│           │   └── css/
│           │       └── style.css
│           ├── templates/
│           │   ├── index.html
│           │   ├── public.html
│           │   └── protected.html
│           └── application.properties
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## How It Works

### OpenID Connect Code Flow

1. **User clicks "Login"**
   - Browser redirects to Keycloak authorization endpoint
   - URL includes client_id, redirect_uri, scope, response_type=code

2. **User authenticates with Keycloak**
   - Keycloak shows login page
   - User enters credentials
   - Keycloak validates credentials

3. **Keycloak issues authorization code**
   - After successful authentication
   - Redirects back to application with code parameter

4. **Application exchanges code for tokens**
   - Makes backend call to Keycloak token endpoint
   - Exchanges authorization code for:
     - **ID Token**: Contains user identity information (JWT)
     - **Access Token**: For accessing protected resources
     - **Refresh Token**: For obtaining new tokens

5. **Application validates ID Token**
   - Verifies signature using Keycloak's public key
   - Extracts user information (claims)
   - Creates authenticated session

6. **User accesses protected resources**
   - Application checks authentication status
   - Grants access to protected pages
   - Displays user information from ID Token

### Security Configuration

The `SecurityConfig` class configures:
- Public endpoints: `/`, `/public`, `/css/**`, `/error`
- Protected endpoints: All others require authentication
- OAuth2 Login: Integrated with Keycloak
- Logout: Clears session and redirects to home

### Components

#### KeycloakOidcDemoApplication.java
Main Spring Boot application class.

#### SecurityConfig.java
Configures Spring Security with OAuth2 client settings:
- Defines which endpoints are public vs protected
- Configures OAuth2 login with Keycloak
- Sets up logout behavior

#### HomeController.java
Handles HTTP requests:
- `/` - Home page
- `/public` - Public page
- `/protected` - Protected page (requires auth)

Extracts user information from `OidcUser` principal:
- Username (preferred_username)
- Email
- Full name
- All claims from ID Token

#### Templates
- `index.html` - Landing page with login status
- `public.html` - Publicly accessible page
- `protected.html` - Protected page showing user info

## Technology Stack

- **Spring Boot**: 3.2.0
- **Spring Security OAuth2 Client**: OIDC integration
- **Thymeleaf**: Template engine for HTML pages
- **Keycloak**: 23.0 - Identity and Access Management
- **Java**: 17
- **Maven**: Build tool
- **Docker**: Containerization
- **Docker Compose**: Multi-container orchestration

## Configuration

### application.properties

Key configurations:
```properties
# Client Registration
spring.security.oauth2.client.registration.keycloak.client-id=demo-client
spring.security.oauth2.client.registration.keycloak.client-secret=<secret>
spring.security.oauth2.client.registration.keycloak.scope=openid,profile,email
spring.security.oauth2.client.registration.keycloak.authorization-grant-type=authorization_code

# Provider Configuration
spring.security.oauth2.client.provider.keycloak.issuer-uri=http://keycloak:8080/realms/demo-realm
```

### Docker Compose Services

**Keycloak:**
- Image: quay.io/keycloak/keycloak:23.0
- Port: 8081 (mapped from container's 8080)
- Admin credentials: admin/admin
- Development mode for easy setup

**Application:**
- Built from Dockerfile
- Port: 8080
- Depends on Keycloak being healthy

## Troubleshooting

### Application can't connect to Keycloak

**Problem**: Application shows connection errors to Keycloak.

**Solution**:
1. Ensure Keycloak is fully started (check logs: `docker compose logs keycloak`)
2. Wait for healthcheck to pass
3. Verify realm name is `demo-realm`
4. Check issuer URI in application.properties

### Redirect URI mismatch

**Problem**: Keycloak shows "Invalid redirect URI" error.

**Solution**:
1. In Keycloak admin console, go to your client
2. Ensure "Valid redirect URIs" includes `http://localhost:8080/*`
3. Ensure "Valid post logout redirect URIs" includes `http://localhost:8080/*`
4. Ensure "Web origins" includes `http://localhost:8080`

### 401 Unauthorized on protected page

**Problem**: Can't access protected page even after login.

**Solution**:
1. Clear browser cookies
2. Ensure you completed login flow
3. Check application logs for authentication errors
4. Verify client secret is correct in application.properties

### Client secret not working

**Problem**: Authentication fails with client authentication error.

**Solution**:
1. Go to Keycloak admin console
2. Navigate to Clients > demo-client > Credentials
3. Copy the current client secret
4. Update application.properties with the correct secret
5. Restart the application: `docker compose restart app`

## Stopping the Services

```bash
# Stop all services
docker compose down

# Stop and remove volumes
docker compose down -v
```

## Development

### Local Development (without Docker)

1. **Start Keycloak**
   ```bash
   docker compose up keycloak
   ```

2. **Run Application locally**
   ```bash
   mvn spring-boot:run
   ```

3. **Update application.properties**
   Change Keycloak URL from `http://keycloak:8080` to `http://localhost:8081`

### Building the Application

```bash
# Build with Maven
mvn clean package

# Build Docker image
docker build -t keycloak-demo .
```

## Learning Outcomes

After completing this project, you understand:

1. **Identity and Access Management (IAM)**
   - Separation of concerns: Identity Provider vs Application
   - Centralized user management

2. **OpenID Connect (OIDC)**
   - OAuth 2.0 Authorization Code Flow
   - ID Tokens, Access Tokens, Refresh Tokens
   - JWT (JSON Web Tokens)

3. **Security Best Practices**
   - Never store passwords in your application
   - Delegate authentication to specialized services
   - Use industry-standard protocols (OAuth2/OIDC)

4. **Spring Security Integration**
   - OAuth2 Client configuration
   - Security filter chains
   - Authentication and authorization

5. **Containerization**
   - Multi-service Docker setup
   - Service dependencies and health checks
   - Container networking

## References

### OpenID Connect & OAuth2
- [OpenID Connect Official](https://openid.net/connect/)
- [OAuth 2.0 RFC](https://datatracker.ietf.org/doc/html/rfc6749)
- [OIDC vs SAML vs OAuth2](https://www.kantega-sso.com/articles/the-difference-between-kerberos-saml-og-openid-connect-oidc)

### Keycloak
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Keycloak Getting Started](https://www.keycloak.org/getting-started/getting-started-docker)

### Spring Security
- [Spring Security OAuth2 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
- [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture)

### JWT
- [JWT Introduction](https://jwt.io/introduction)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)

## License

This project is created for educational purposes as part of Modul 321: Verteilte Systeme programmieren.
