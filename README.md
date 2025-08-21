# Spring Boot WebFlux Person API

A production-ready RESTful API built with Spring Boot and WebFlux for managing person records reactively.

## Technologies Used

- Java 24
- Spring Boot 3.5.0
- Spring WebFlux (Reactive Programming)
- Spring Data R2DBC (Reactive Database Access)
- Spring Security
- Spring Boot Actuator (Monitoring)
- PostgreSQL / H2 Database
- Docker & Docker Compose
- Maven
- OpenAPI 3 Documentation

## Features

- **Reactive Programming**: Non-blocking I/O using Reactor (Mono/Flux)
- **CRUD Operations**: Complete REST API for Person entities
- **Database Integration**: R2DBC with PostgreSQL/H2 support
- **Input Validation**: Bean validation with custom error handling
- **Security**: Spring Security configuration
- **Monitoring**: Actuator endpoints with health checks
- **API Documentation**: OpenAPI 3 with Swagger UI
- **Testing**: Comprehensive unit and integration tests
- **Containerization**: Docker support with multi-stage builds
- **Production Ready**: Profiles, logging, error handling

## API Endpoints

| Method | URL                   | Description                   | Request Body                        | Response                                |
|--------|------------------------|-------------------------------|------------------------------------|-----------------------------------------|
| POST   | `/api/v1/persons`       | Create a new person           | `{"name": "Name", "email": "email@example.com"}` | Person object with generated ID         |
| GET    | `/api/v1/persons`       | Get all people                | None                               | List of all people                      |
| GET    | `/api/v1/persons/{id}`  | Get person by ID              | None                               | Person with specified ID                |
| PUT    | `/api/v1/persons/{id}`  | Update person                 | `{"name": "Name", "email": "email@example.com"}` | Updated person                          |
| DELETE | `/api/v1/persons/{id}`  | Delete person                 | None                               | Success/failure message                 |

## Getting Started

### Prerequisites

- Java 24 or higher
- Maven

### Running the Application

#### Option 1: Using Docker Compose (Recommended)
```bash
# Clone and navigate to project
git clone https://github.com/David-Kamau-Builds/spring-webflux-person-api.git
cd spring-webflux-person-api

# Setup environment variables
setup-env.bat  # Windows
# or manually: cp .env.example .env

# Edit .env file with your secure values
# Then build and run with PostgreSQL
docker-compose up --build
```

#### Option 2: Local Development
```bash
# Run with H2 in-memory database
mvn spring-boot:run

# Or run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Option 3: Production Build
```bash
# Windows
build.bat

# Manual build
mvn clean package
java -jar target/demo-*.jar --spring.profiles.active=prod
```

### Environment Configuration

The application uses environment variables for configuration. Copy `.env.example` to `.env` and update with your values:

```bash
# Database Configuration
DATABASE_URL=r2dbc:postgresql://localhost:5432/persondb
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your_secure_password

# Application Configuration
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
LOG_LEVEL=INFO
```

**Important**: Never commit `.env` files to version control. Only `.env.example` should be committed.

### Accessing the Application
- **API**: http://localhost:8080/api/v1/persons
- **API Documentation**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **Metrics**: http://localhost:8080/actuator/metrics

## Example Requests

### Create a Person
```
POST /api/v1/persons
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john.doe@example.com"
}
```

### Get All People
```
GET /api/v1/persons
```

### Get Person by ID
```
GET /api/v1/persons/123e4567-e89b-12d3-a456-426614174000
```

### Update Person
```
PUT /api/v1/persons/123e4567-e89b-12d3-a456-426614174000
Content-Type: application/json

{
  "name": "Jane Doe",
  "email": "jane.doe@example.com"
}
```

### Delete Person
```
DELETE /api/v1/persons/123e4567-e89b-12d3-a456-426614174000
```

## CI/CD Pipeline

This project includes comprehensive GitHub Actions workflows:

### 🔄 **Continuous Integration**
- **Tests**: Unit and integration tests with PostgreSQL
- **Security Scanning**: OWASP dependency check, Snyk, Trivy
- **Code Quality**: SpotBugs, Checkstyle, test coverage
- **Secret Detection**: TruffleHog for leaked credentials
- **Docker Security**: Container vulnerability scanning

### 🚀 **Workflows**
- `ci.yml` - Main CI pipeline (test, build, security)
- `security.yml` - Dedicated security scans
- `code-quality.yml` - Linting, formatting, coverage

### 📋 **Quality Gates**
- All tests must pass
- No high-severity security vulnerabilities
- Code formatting compliance
- No secrets in commits

### 🛠️ **Local Development**
```bash
# Run pre-commit checks
scripts\pre-commit.bat

# Fix code formatting
mvn spotless:apply

# Run security scan
mvn org.owasp:dependency-check-maven:check
```

## License

This project is licensed under the MIT License - see the LICENSE file for details.
