# Spring Boot WebFlux Person API

A simple RESTful API built with Spring Boot and WebFlux for managing person records reactively.

## Technologies Used

- Java 24
- Spring Boot 3.5.0
- Spring WebFlux
- Maven

## Features

- Reactive endpoints using Reactor (Mono/Flux)
- CRUD operations for Person entities
- In-memory data storage
- Consistent JSON response format

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

1. Clone the repository
   ```
   git clone https://github.com/David-Kamau-Builds/spring-webflux-person-api.git
   cd (custom-folder-name)/spring-webflux-person-api
   ```

2. Build the project
   ```
   mvn clean install
   ```

3. Run the application
   ```
   mvn spring-boot:run
   ```

4. The API will be available at `http://localhost:8080/api/v1/persons`

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

## License

This project is licensed under the MIT License - see the LICENSE file for details.
