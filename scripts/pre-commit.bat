@echo off
echo Running pre-commit checks...

echo 1. Checking for secrets...
git diff --cached --name-only | findstr /i "\.env$" >nul
if %errorlevel% equ 0 (
    echo ERROR: .env file detected in commit! Remove it before committing.
    exit /b 1
)

echo 2. Running tests...
mvn clean test -q
if %errorlevel% neq 0 (
    echo ERROR: Tests failed!
    exit /b 1
)

echo 3. Checking code formatting...
mvn spotless:check -q
if %errorlevel% neq 0 (
    echo WARNING: Code formatting issues detected. Run 'mvn spotless:apply' to fix.
)

echo 4. Running security scan...
mvn org.owasp:dependency-check-maven:check -q
if %errorlevel% neq 0 (
    echo WARNING: Security vulnerabilities detected. Check the report.
)

echo Pre-commit checks completed!