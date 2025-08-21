# Security Guidelines

## Environment Variables

### ✅ Safe to commit:
- `.env.example` - Template with placeholder values
- `application.yml` - Uses environment variable references

### ❌ Never commit:
- `.env` - Contains actual secrets
- `.env.local`, `.env.production` - Environment-specific secrets
- Any files with actual passwords, API keys, or secrets

## Before Pushing to GitHub

1. **Check .gitignore**: Ensure `.env` is listed
2. **Verify no secrets**: Run `git status` to check staged files
3. **Use placeholders**: Only commit `.env.example` with `change_me` values
4. **Review commits**: Check diff before pushing

## Production Deployment

1. **Set environment variables** in your deployment platform
2. **Use secure password generation** for database passwords
3. **Enable authentication** by updating SecurityConfig
4. **Use HTTPS** in production
5. **Enable security headers** and CORS properly
6. **Monitor logs** for security events

## Environment Variable Security

- Use strong, unique passwords (min 16 characters)
- Rotate secrets regularly
- Use different secrets for each environment
- Never log sensitive environment variables
- Use secret management services in production (AWS Secrets Manager, etc.)