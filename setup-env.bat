@echo off
echo Setting up environment for Spring WebFlux Person API...

if not exist .env (
    echo Creating .env file from template...
    copy .env.example .env
    echo.
    echo Please edit .env file with your actual values:
    echo - DATABASE_PASSWORD: Set a secure password
    echo - JWT_SECRET: Set a secure JWT secret
    echo - POSTGRES_PASSWORD: Set a secure database password
    echo.
    echo After editing .env, run: docker-compose up --build
) else (
    echo .env file already exists.
)

echo.
echo Environment setup complete!
pause