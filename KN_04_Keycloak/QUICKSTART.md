# KN_04: Quick Start Guide

## TL;DR - Fast Setup

```bash
# 1. Start services
cd KN_04_Keycloak
docker compose up --build

# 2. Wait for Keycloak to start (about 60 seconds)

# 3. Configure Keycloak (see detailed steps below)

# 4. Update client secret in src/main/resources/application.properties

# 5. Restart app
docker compose restart app

# 6. Access application at http://localhost:8080
```

## Keycloak Configuration (Required)

### Access Keycloak Admin Console
- URL: http://localhost:8081
- Username: `admin`
- Password: `admin`

### Create Realm
1. Click dropdown in top-left (says "master")
2. Click "Create Realm"
3. Name: `demo-realm`
4. Click "Create"

### Create Client
1. Go to "Clients" → "Create client"
2. Settings:
   - Client ID: `demo-client`
   - Client Type: `OpenID Connect`
   - Click "Next"
3. Capability config:
   - Client authentication: `ON`
   - Authentication flow: Check "Standard flow" and "Direct access grants"
   - Click "Next"
4. Login settings:
   - Valid redirect URIs: `http://localhost:8080/*`
   - Valid post logout redirect URIs: `http://localhost:8080/*`
   - Web origins: `http://localhost:8080`
   - Click "Save"

### Get Client Secret
1. Go to client "Credentials" tab
2. Copy the "Client secret"
3. Update in `src/main/resources/application.properties`:
   ```properties
   spring.security.oauth2.client.registration.keycloak.client-secret=<paste-secret-here>
   ```

### Create Test User
1. Go to "Users" → "Add user"
2. Settings:
   - Username: `testuser`
   - Email: `testuser@example.com`
   - First name: `Test`
   - Last name: `User`
   - Email verified: `ON`
   - Click "Create"
3. Go to "Credentials" tab
4. Click "Set password"
   - Password: `password`
   - Temporary: `OFF`
   - Click "Save"

## Test the Application

### 1. Home Page
- URL: http://localhost:8080
- Should show welcome message

### 2. Public Page
- URL: http://localhost:8080/public
- Accessible without login

### 3. Login
- Click "Login" button
- Enter credentials: `testuser` / `password`

### 4. Protected Page
- URL: http://localhost:8080/protected
- Shows user information from ID Token

## Common Issues

### Can't connect to Keycloak
- Wait longer for Keycloak to start
- Check: `docker compose logs keycloak`

### Invalid redirect URI error
- Verify redirect URIs in Keycloak client config
- Must include `http://localhost:8080/*`

### Login fails
- Check client secret in application.properties
- Verify it matches Keycloak client credentials

### Need to reconfigure?
```bash
# Stop everything
docker compose down -v

# Start fresh
docker compose up --build
```

## What You're Learning

✅ Identity and Access Management (IAM)  
✅ OpenID Connect (OIDC) protocol  
✅ OAuth 2.0 Authorization Code Flow  
✅ JWT (JSON Web Tokens)  
✅ Spring Security OAuth2 Client  
✅ Keycloak Identity Provider  

## Access Points

- **Application**: http://localhost:8080
- **Keycloak Admin**: http://localhost:8081
- **Test User**: testuser / password
- **Admin User**: admin / admin

For detailed documentation, see [README.md](README.md)
