# Project Structure

## Directory Layout

```
spring-webflux-person-api/
├── .github/workflows/          # GitHub Actions CI/CD pipelines
│   ├── ci.yml                 # Main CI pipeline
│   ├── security.yml           # Security scanning
│   └── code-quality.yml       # Code quality checks
├── src/
│   ├── main/
│   │   ├── java/com/start/demo/
│   │   │   ├── api/           # REST Controllers
│   │   │   │   └── PersonController.java
│   │   │   ├── config/        # Configuration classes
│   │   │   │   ├── DatabaseConfig.java
│   │   │   │   └── SecurityConfig.java
│   │   │   ├── exception/     # Global exception handling
│   │   │   │   └── GlobalExceptionHandler.java
│   │   │   ├── model/         # Domain entities
│   │   │   │   └── Person.java
│   │   │   ├── repository/    # Data access layer
│   │   │   │   └── PersonRepository.java
│   │   │   ├── service/       # Business logic layer
│   │   │   │   └── PersonService.java
│   │   │   └── DemoApplication.java
│   │   └── resources/
│   │       ├── application.yml    # Application configuration
│   │       └── schema.sql         # Database schema
│   └── test/
│       ├── java/com/start/demo/
│       │   ├── service/
│       │   │   └── PersonServiceTest.java
│       │   └── DemoApplicationTests.java
│       └── resources/
│           └── application-test.yml
├── scripts/
│   └── pre-commit.bat         # Pre-commit validation script
├── .env.example               # Environment variables template
├── .gitignore                 # Git ignore rules
├── .yamllint.yml             # YAML linting configuration
├── build.bat                  # Windows build script
├── docker-compose.yml         # Docker orchestration
├── Dockerfile                 # Container definition
├── owasp-suppressions.xml     # Security scan suppressions
├── pom.xml                    # Maven configuration
├── setup-env.bat             # Environment setup script
└── README.md                  # Project documentation
```

## Architecture Layers

### 1. **API Layer** (`api/`)
- **PersonController**: REST endpoints for CRUD operations
- Handles HTTP requests/responses
- Input validation and error handling
- OpenAPI documentation annotations

### 2. **Service Layer** (`service/`)
- **PersonService**: Business logic implementation
- Orchestrates data operations
- Transaction management
- Business rule enforcement

### 3. **Repository Layer** (`repository/`)
- **PersonRepository**: Data access interface
- R2DBC reactive database operations
- Extends ReactiveCrudRepository

### 4. **Model Layer** (`model/`)
- **Person**: Domain entity with validation
- Database mapping annotations
- Bean validation constraints

### 5. **Configuration Layer** (`config/`)
- **DatabaseConfig**: R2DBC configuration
- **SecurityConfig**: Security settings

### 6. **Exception Handling** (`exception/`)
- **GlobalExceptionHandler**: Centralized error handling
- Validation error processing
- Consistent error responses

## Code Organization Principles

### ✅ **Clean Architecture**
- Clear separation of concerns
- Dependency inversion principle
- Reactive programming patterns

### ✅ **Consistent Formatting**
- Google Java Format applied
- Organized imports
- Consistent indentation and spacing

### ✅ **Naming Conventions**
- PascalCase for classes
- camelCase for methods and variables
- Descriptive and meaningful names

### ✅ **Package Structure**
- Feature-based organization
- Clear layer separation
- Logical grouping of related classes

## Quality Standards

- **Code Formatting**: Google Java Format
- **Static Analysis**: SpotBugs, Checkstyle
- **Security Scanning**: OWASP, Snyk, Trivy
- **Test Coverage**: JaCoCo reporting
- **Documentation**: OpenAPI 3, JavaDoc