@echo off
echo Building Spring WebFlux Person API...

echo Running tests...
mvn clean test

if %ERRORLEVEL% neq 0 (
    echo Tests failed! Build aborted.
    exit /b 1
)

echo Building application...
mvn clean package -DskipTests

if %ERRORLEVEL% neq 0 (
    echo Build failed!
    exit /b 1
)

echo Building Docker image...
docker build -t person-api:latest .

if %ERRORLEVEL% neq 0 (
    echo Docker build failed!
    exit /b 1
)

echo Build completed successfully!
echo To run: docker-compose up