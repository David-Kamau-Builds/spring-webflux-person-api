# Security notes

This repository is a demo project. For interview-ready or production submissions, follow these steps:

- Do NOT commit secrets. Use environment variables for secrets (e.g., JWT secret via `JWT_SECRET`).
- Rotate keys regularly and use a secrets manager in production.
- Use HTTPS and secure cookie flags when deploying.
- Add dependency scanning (e.g., GitHub Dependabot, Snyk, or Trivy in CI).
- Limit log output to avoid leaking sensitive data.

Minimal step to improve this repo now:
- Move the hard-coded JWT secret in `JwtUtil` to `application.yml` or environment variables.
- Use strong random values (>=32 bytes) and Base64-encode them when setting via env var.
