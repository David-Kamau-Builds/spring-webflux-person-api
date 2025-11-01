# Security Configuration Guide

## Environment Variables

This application requires the following environment variables to be set for secure operation:

### Required Variables

- `DB_PASSWORD`: Database password (required)
- `ADMIN_PASSWORD`: Admin user password (required)
- `JWT_SECRET`: Base64-encoded JWT signing secret (required)

### Optional Variables

- `JWT_EXPIRATION`: JWT token expiration time in milliseconds (default: 86400000 = 24 hours)
- `TEST_USERNAME`: Test user username (default: testuser)
- `TEST_PASSWORD`: Test user password (default: testpassword)

## Setup Instructions

1. Copy `.env.example` to `.env`
2. Fill in all required environment variables with secure values
3. Ensure `.env` is in your `.gitignore` (already configured)

## Security Best Practices

- Never commit sensitive credentials to version control
- Use strong, unique passwords for all accounts
- Generate a secure JWT secret using: `openssl rand -base64 64`
- Rotate JWT secrets regularly in production
- Use environment-specific configuration files
- Enable HTTPS in production environments

## Production Deployment

For production deployments:
- Use a secrets management system (AWS Secrets Manager, HashiCorp Vault, etc.)
- Set environment variables through your deployment platform
- Never use default or example values in production
- Enable audit logging and monitoring
- Implement proper backup and disaster recovery procedures