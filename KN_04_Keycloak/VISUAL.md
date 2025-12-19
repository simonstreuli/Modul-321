# KN_04: Visual Documentation

## Application Flow Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      User's Browser                          │
└────────────┬────────────────────────────────────────────────┘
             │
             │ 1. Access http://localhost:8080
             │
             v
┌─────────────────────────────────────────────────────────────┐
│                   Spring Boot Application                    │
│                      (Port 8080)                             │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              SecurityConfig                          │  │
│  │  ┌──────────────────────────────────────────────┐  │  │
│  │  │ Public Routes:                               │  │  │
│  │  │  - /                                         │  │  │
│  │  │  - /public                                   │  │  │
│  │  │  - /css/**                                   │  │  │
│  │  └──────────────────────────────────────────────┘  │  │
│  │  ┌──────────────────────────────────────────────┐  │  │
│  │  │ Protected Routes:                            │  │  │
│  │  │  - /protected (requires authentication)      │  │  │
│  │  └──────────────────────────────────────────────┘  │  │
│  └──────────────────────────────────────────────────────┘  │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐  │
│  │              HomeController                          │  │
│  │  - GET /         → index.html                       │  │
│  │  - GET /public   → public.html                      │  │
│  │  - GET /protected → protected.html                  │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────┬────────────────────────────────────────────────┘
             │
             │ 2. Click "Login"
             │
             v
┌─────────────────────────────────────────────────────────────┐
│         OAuth2 Login Flow (Spring Security)                  │
│                                                              │
│  3. Redirect to Keycloak Authorization Endpoint             │
│     http://keycloak:8080/realms/demo-realm/protocol/        │
│     openid-connect/auth?                                    │
│     - client_id=demo-client                                 │
│     - redirect_uri=http://localhost:8080/login/oauth2/code/ │
│     - response_type=code                                    │
│     - scope=openid profile email                            │
└────────────┬────────────────────────────────────────────────┘
             │
             v
┌─────────────────────────────────────────────────────────────┐
│                  Keycloak Server                             │
│                  (Port 8081)                                 │
│                                                              │
│  4. User enters credentials:                                │
│     Username: testuser                                      │
│     Password: password                                      │
│                                                              │
│  5. Keycloak validates credentials                          │
│                                                              │
│  6. Keycloak generates Authorization Code                   │
│                                                              │
│  7. Redirect back to application with code                  │
│     http://localhost:8080/login/oauth2/code/keycloak        │
│     ?code=<authorization_code>                              │
└────────────┬────────────────────────────────────────────────┘
             │
             v
┌─────────────────────────────────────────────────────────────┐
│         Token Exchange (Spring Security)                     │
│                                                              │
│  8. Application sends code to Keycloak Token Endpoint       │
│     POST http://keycloak:8080/realms/demo-realm/protocol/   │
│          openid-connect/token                               │
│     Body:                                                   │
│     - grant_type=authorization_code                         │
│     - code=<authorization_code>                             │
│     - client_id=demo-client                                 │
│     - client_secret=<secret>                                │
│                                                              │
│  9. Keycloak responds with tokens:                          │
│     {                                                       │
│       "id_token": "<JWT>",                                  │
│       "access_token": "<JWT>",                              │
│       "refresh_token": "<JWT>",                             │
│       "expires_in": 300                                     │
│     }                                                       │
└────────────┬────────────────────────────────────────────────┘
             │
             v
┌─────────────────────────────────────────────────────────────┐
│         ID Token Validation & Session Creation               │
│                                                              │
│  10. Spring Security validates ID Token:                    │
│      - Verifies signature using Keycloak's public key       │
│      - Validates issuer, audience, expiration              │
│                                                              │
│  11. Extracts user claims from ID Token:                    │
│      {                                                      │
│        "sub": "user-uuid",                                  │
│        "preferred_username": "testuser",                    │
│        "email": "testuser@example.com",                     │
│        "name": "Test User",                                 │
│        "given_name": "Test",                                │
│        "family_name": "User",                               │
│        ...                                                  │
│      }                                                      │
│                                                              │
│  12. Creates authenticated session                          │
│      - Principal: OidcUser with claims                      │
│      - Session stored in memory                             │
└────────────┬────────────────────────────────────────────────┘
             │
             │ 13. Redirect to original requested page
             │
             v
┌─────────────────────────────────────────────────────────────┐
│              Protected Page Rendered                         │
│                                                              │
│  14. Access granted to /protected                           │
│                                                              │
│  15. Controller extracts user info from OidcUser:           │
│      - Username: principal.getPreferredUsername()           │
│      - Email: principal.getEmail()                          │
│      - Full Name: principal.getFullName()                   │
│      - All Claims: principal.getClaims()                    │
│      - ID Token: principal.getIdToken()                     │
│                                                              │
│  16. Renders protected.html with user information           │
└─────────────────────────────────────────────────────────────┘
```

## Page Screenshots Description

### Home Page (/)
- **Header**: "KN_04: IAM with Keycloak" with subtitle "OpenID Connect Demo"
- **Navigation**: Links to Home, Public Page, Protected Page (if logged in)
- **Welcome Box**: 
  - Not logged in: Shows info message and login link
  - Logged in: Shows success message with username
- **Info Box**: Explains features and security concepts
- **Styling**: Purple gradient header, clean white content boxes

### Public Page (/public)
- **Accessible without login**
- **Content**: Information about public access
- **Message**: "This is a public page accessible to everyone!"
- **Security Note**: Explains public vs protected resources
- **Action Box**: Different content based on auth status

### Protected Page (/protected)
- **Requires Authentication**
- **User Information Table**:
  ```
  Username:   testuser
  Email:      testuser@example.com
  Full Name:  Test User
  ```
- **Token Information**:
  - Expandable "View ID Token Claims" section
  - Shows all JWT claims in a table
  - Expandable "View Raw ID Token" section
  - Shows the raw JWT string
  - Security warning about token exposure
- **Info Box**: Explains what happened during OIDC flow
- **Styling**: Green success messages, expandable sections

## Keycloak Admin Console

### Realm Configuration
```
Realm name: demo-realm
- Users: testuser
- Clients: demo-client
  - Client Protocol: openid-connect
  - Access Type: confidential
  - Valid Redirect URIs: http://localhost:8080/*
  - Valid Post Logout URIs: http://localhost:8080/*
  - Web Origins: http://localhost:8080
```

### User Details
```
Username: testuser
Email: testuser@example.com
First Name: Test
Last Name: User
Email Verified: Yes
Enabled: Yes
```

## Technology Stack Visual

```
┌─────────────────────────────────────────┐
│           Frontend Layer                │
│                                         │
│  Thymeleaf Templates                    │
│  ├─ index.html                          │
│  ├─ public.html                         │
│  └─ protected.html                      │
│                                         │
│  Static Resources                       │
│  └─ style.css                           │
└─────────────────────────────────────────┘
              │
              │
┌─────────────────────────────────────────┐
│         Application Layer               │
│                                         │
│  Spring Boot 3.2.0                      │
│  ├─ Spring Security                     │
│  ├─ Spring OAuth2 Client                │
│  └─ Spring Web MVC                      │
│                                         │
│  Controllers                            │
│  └─ HomeController                      │
│                                         │
│  Configuration                          │
│  └─ SecurityConfig                      │
└─────────────────────────────────────────┘
              │
              │ OAuth2/OIDC Protocol
              │
┌─────────────────────────────────────────┐
│      Identity Provider Layer            │
│                                         │
│  Keycloak 23.0                          │
│  ├─ Realm: demo-realm                   │
│  ├─ Client: demo-client                 │
│  └─ User: testuser                      │
│                                         │
│  Features:                              │
│  ├─ User Authentication                 │
│  ├─ Token Issuance (JWT)                │
│  ├─ Token Validation                    │
│  └─ OIDC Discovery                      │
└─────────────────────────────────────────┘
              │
              │
┌─────────────────────────────────────────┐
│       Infrastructure Layer              │
│                                         │
│  Docker Compose                         │
│  ├─ Keycloak Container                  │
│  │  └─ Port 8081:8080                   │
│  └─ Application Container               │
│     └─ Port 8080:8080                   │
│                                         │
│  Network: keycloak-network              │
└─────────────────────────────────────────┘
```

## Security Features

### Authentication Flow
1. ✅ Authorization Code Flow (most secure)
2. ✅ PKCE (Proof Key for Code Exchange) supported
3. ✅ State parameter for CSRF protection
4. ✅ Nonce for replay attack prevention

### Token Security
1. ✅ JWT tokens cryptographically signed
2. ✅ Token signature verification
3. ✅ Token expiration validation
4. ✅ Issuer and audience validation

### Application Security
1. ✅ CSRF protection enabled
2. ✅ Session management
3. ✅ Secure logout
4. ✅ HTTPS-ready (for production)

### Best Practices Applied
1. ✅ No passwords stored in application
2. ✅ Centralized identity management
3. ✅ Environment variables for secrets
4. ✅ Production-ready logging configuration
5. ✅ Security warnings for demo features

## File Structure

```
KN_04_Keycloak/
├── src/main/
│   ├── java/ch/modul321/keycloak/
│   │   ├── KeycloakOidcDemoApplication.java    [Main App]
│   │   ├── config/
│   │   │   └── SecurityConfig.java             [Security Setup]
│   │   └── controller/
│   │       └── HomeController.java             [Endpoints]
│   └── resources/
│       ├── application.properties              [Config]
│       ├── static/css/
│       │   └── style.css                       [Styling]
│       └── templates/
│           ├── index.html                      [Home]
│           ├── public.html                     [Public]
│           └── protected.html                  [Protected]
├── Dockerfile                                   [App Container]
├── docker-compose.yml                          [Multi-Container]
├── pom.xml                                     [Dependencies]
├── README.md                                   [Full Docs]
├── QUICKSTART.md                               [Quick Guide]
├── SUMMARY.md                                  [Implementation]
├── VISUAL.md                                   [This File]
└── setup-guide.sh                              [Setup Helper]
```

## Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| Application | http://localhost:8080 | - |
| Public Page | http://localhost:8080/public | - |
| Protected Page | http://localhost:8080/protected | testuser/password |
| Keycloak Admin | http://localhost:8081 | admin/admin |

## Color Scheme

- **Primary**: Purple (#667eea to #764ba2)
- **Success**: Green (#d4edda)
- **Info**: Blue (#d1ecf1)
- **Warning**: Yellow (#fff3cd)
- **Background**: White (#ffffff)
- **Text**: Dark Gray (#333333)
