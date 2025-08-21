# Simple Spring WebFlux Person API

A minimal RESTful API built with Spring Boot and WebFlux for managing person records.

## Technologies

- Java 21
- Spring Boot 3.5.0
- Spring WebFlux (Reactive Programming)
- Spring Data R2DBC
- H2 Database (In-Memory)

## API Endpoints

| Method | URL                   | Description       |
|--------|-----------------------|-------------------|
| POST   | `/api/v1/persons`     | Create a person   |
| GET    | `/api/v1/persons`     | Get all people    |
| GET    | `/api/v1/persons/{id}`| Get person by ID  |
| PUT    | `/api/v1/persons/{id}`| Update person     |
| DELETE | `/api/v1/persons/{id}`| Delete person     |

## Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## Example Usage

### Create a Person
```bash
curl -X POST http://localhost:8080/api/v1/persons \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com"}'
```

### Get All People
```bash
curl http://localhost:8080/api/v1/persons
```

### Get Person by ID
```bash
curl http://localhost:8080/api/v1/persons/{id}
```

### Update Person
```bash
curl -X PUT http://localhost:8080/api/v1/persons/{id} \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane Doe","email":"jane@example.com"}'
```

### Delete Person
```bash
curl -X DELETE http://localhost:8080/api/v1/persons/{id}
```