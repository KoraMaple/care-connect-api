# Environment-Specific Configuration Guide

This Spring Boot application supports environment-specific configurations for development, testing, and production environments.

## Available Profiles

### 1. Development Profile (`dev`)
- **Database**: H2 in-memory database for easy development
- **Security**: Enabled with more permissive public endpoints
- **H2 Console**: Enabled at `/h2-console`
- **Logging**: Verbose SQL logging enabled

### 2. Test Profile (`test`)
- **Database**: H2 in-memory database for testing
- **Security**: Completely disabled for easier testing
- **Logging**: Minimal logging
- **Server Port**: Random available port (0)

### 3. Production Profile (`prod`)
- **Database**: PostgreSQL (requires environment variables)
- **Security**: Strict security with minimal public endpoints
- **Logging**: Production-level logging
- **CORS**: Restricted to authorized origins only

## How to Switch Profiles

### Option 1: Environment Variable
Set the `SPRING_PROFILES_ACTIVE` environment variable:

```bash
# Development
export SPRING_PROFILES_ACTIVE=dev

# Testing  
export SPRING_PROFILES_ACTIVE=test

# Production
export SPRING_PROFILES_ACTIVE=prod
```

### Option 2: Command Line Argument
Pass the profile when running the application:

```bash
# Development
java -jar coreapi.jar --spring.profiles.active=dev

# Testing
java -jar coreapi.jar --spring.profiles.active=test

# Production  
java -jar coreapi.jar --spring.profiles.active=prod
```

### Option 3: Maven (for development)
```bash
# Development
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Testing
mvn test -Dspring.profiles.active=test
```

### Option 4: IDE Configuration
In your IDE (IntelliJ IDEA, Eclipse, VS Code), set the active profile in the run configuration:
- **Program arguments**: `--spring.profiles.active=dev`
- **Environment variables**: `SPRING_PROFILES_ACTIVE=dev`

## Configuration Properties by Profile

### Security Configuration
Each profile has different security settings controlled by:
- `app.security.enabled`: true/false to enable/disable security entirely
- `app.security.public-endpoints`: comma-separated list of endpoints that don't require authentication

#### Development (`dev`)
```properties
app.security.enabled=true
app.security.public-endpoints=/api/test/**,/api/public/**,/health,/actuator/**,/h2-console/**,/api/users/test,/api/children/test,/api/facilities/test,/api/guardians/test
```

#### Testing (`test`)
```properties
app.security.enabled=false
app.security.public-endpoints=/**
```

#### Production (`prod`)
```properties
app.security.enabled=true
app.security.public-endpoints=/health,/actuator/health,/actuator/info
```

## Environment Variables Required

### Development
- No environment variables required (uses defaults)

### Testing
- No environment variables required (uses H2 in-memory)

### Production
- `DATABASE_URL`: PostgreSQL connection URL
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `CLERK_API_SECRET_KEY`: Clerk authentication secret key
- `CLERK_AUTHORIZED_PARTIES`: Allowed CORS origins

## Examples

### Running in Development Mode
1. Set profile: `export SPRING_PROFILES_ACTIVE=dev`
2. Run: `mvn spring-boot:run`
3. Access H2 Console: `http://localhost:8080/h2-console`
4. Test endpoints work without authentication

### Running Tests
1. Tests automatically use the `test` profile
2. Run: `mvn test`
3. All endpoints are accessible without authentication

### Production Deployment
1. Set required environment variables
2. Set profile: `export SPRING_PROFILES_ACTIVE=prod`
3. Run: `java -jar coreapi.jar`
4. Only `/health` and `/actuator/health` endpoints are public

## Adding New Public Endpoints

To add new public endpoints, update the appropriate profile's properties file:

```properties
# In application-dev.properties, application-test.properties, or application-prod.properties
app.security.public-endpoints=/health,/actuator/**,/api/public/**,/your-new-endpoint/**
```

The endpoints support Ant-style path patterns:
- `/**` matches all paths
- `/api/test/**` matches all paths starting with `/api/test/`
- `/api/users/*/profile` matches `/api/users/123/profile`
- `/api/public` matches exactly `/api/public`

## Troubleshooting

### Profile Not Loading
- Check that the profile name is correct (`dev`, `test`, `prod`)
- Verify the `application-{profile}.properties` file exists
- Check that environment variables are properly set

### Authentication Issues
- In `dev`: Check if your endpoint is listed in `app.security.public-endpoints`
- In `test`: Security should be completely disabled
- In `prod`: Ensure you have proper Clerk authentication headers

### Database Issues
- `dev`/`test`: Check H2 console at `/h2-console` (dev only)
- `prod`: Verify DATABASE_URL and credentials are correct
