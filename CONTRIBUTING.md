# Contributing & Local setup

Small guide to run the project locally and run tests. Good to include in interview submissions.

Prerequisites
- Java 21
- Maven 3.6+

Run the application

```bash
mvn -q spring-boot:run
```

Run tests

```bash
mvn -q test
```

Notes
- The application uses an in-memory H2 database by default for fast startup and deterministic tests.
- JWT secret and expiration can be configured via `application.yml` or environment variables `JWT_SECRET` and `JWT_EXPIRATION`.

*** If you plan to extend: make small, focused PRs and include tests.***
