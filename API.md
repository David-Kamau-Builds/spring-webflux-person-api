# 📚 API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
All endpoints require basic authentication:
- **Username**: `admin`
- **Password**: `admin123`

## Employee Management Endpoints

### Create Employee
**POST** `/employees`

Creates a new employee record.

**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+1234567890",
  "departmentId": "550e8400-e29b-41d4-a716-446655440001",
  "position": "SENIOR_DEVELOPER",
  "salary": 85000.00,
  "hireDate": "2024-01-15",
  "status": "ACTIVE"
}
```

**Response:** `201 Created`
```json
{
  "id": "660e8400-e29b-41d4-a716-446655440001",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@company.com",
  "phone": "+1234567890",
  "departmentId": "550e8400-e29b-41d4-a716-446655440001",
  "position": "SENIOR_DEVELOPER",
  "salary": 85000.00,
  "hireDate": "2024-01-15",
  "status": "ACTIVE"
}
```

### Get All Employees
**GET** `/employees`

Retrieves all employees with department information.

**Response:** `200 OK`
```json
[
  {
    "id": "660e8400-e29b-41d4-a716-446655440001",
    "firstName": "John",
    "lastName": "Doe",
    "fullName": "John Doe",
    "email": "john.doe@company.com",
    "phone": "+1234567890",
    "departmentName": "Engineering",
    "position": "SENIOR_DEVELOPER",
    "salary": 85000.00,
    "hireDate": "2024-01-15",
    "status": "ACTIVE"
  }
]
```

### Get Employee by ID
**GET** `/employees/{id}`

Retrieves a specific employee by ID.

**Response:** `200 OK` (same as above single employee object)

### Update Employee
**PUT** `/employees/{id}`

Updates an existing employee record.

**Request Body:** Same as Create Employee
**Response:** `200 OK` (updated employee object)

### Delete Employee
**DELETE** `/employees/{id}`

Deletes an employee record.

**Response:** `204 No Content`

### Search Employees
**GET** `/employees/search?name={name}`

Searches employees by name (first or last name).

**Parameters:**
- `name` (string): Name to search for

**Response:** `200 OK` (array of matching employees)

### Get Employees by Department
**GET** `/employees/department/{departmentId}`

Retrieves all employees in a specific department.

**Response:** `200 OK` (array of employees)

### Get Employees by Status
**GET** `/employees/status/{status}`

Retrieves all employees with a specific status.

**Parameters:**
- `status`: `ACTIVE`, `INACTIVE`, `ON_LEAVE`, `TERMINATED`

**Response:** `200 OK` (array of employees)

### Get Employees by Salary Range
**GET** `/employees/salary-range?minSalary={min}&maxSalary={max}`

Retrieves employees within a salary range.

**Parameters:**
- `minSalary` (decimal): Minimum salary
- `maxSalary` (decimal): Maximum salary

**Response:** `200 OK` (array of employees)

### Get Employee Count by Department
**GET** `/employees/department/{departmentId}/count`

Returns the number of employees in a department.

**Response:** `200 OK`
```json
15
```

### Get Average Salary by Department
**GET** `/employees/department/{departmentId}/average-salary`

Returns the average salary for a department.

**Response:** `200 OK`
```json
87500.00
```

## Department Management Endpoints

### Create Department
**POST** `/departments`

Creates a new department.

**Request Body:**
```json
{
  "name": "Engineering",
  "description": "Software development and technical operations",
  "managerId": "660e8400-e29b-41d4-a716-446655440001"
}
```

**Response:** `201 Created`
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440001",
  "name": "Engineering",
  "description": "Software development and technical operations",
  "managerId": "660e8400-e29b-41d4-a716-446655440001"
}
```

### Get All Departments
**GET** `/departments`

Retrieves all departments.

**Response:** `200 OK` (array of departments)

### Get Department by ID
**GET** `/departments/{id}`

Retrieves a specific department by ID.

**Response:** `200 OK` (department object)

### Update Department
**PUT** `/departments/{id}`

Updates an existing department.

**Request Body:** Same as Create Department
**Response:** `200 OK` (updated department object)

### Delete Department
**DELETE** `/departments/{id}`

Deletes a department.

**Response:** `204 No Content`

### Search Departments
**GET** `/departments/search?name={name}`

Searches departments by name.

**Parameters:**
- `name` (string): Name to search for

**Response:** `200 OK` (array of matching departments)

## Data Models

### Employee
```json
{
  "id": "UUID",
  "firstName": "string (max 50 chars, required)",
  "lastName": "string (max 50 chars, required)",
  "email": "string (valid email, max 255 chars, required)",
  "phone": "string (valid phone format, required)",
  "departmentId": "UUID (required)",
  "position": "enum (required)",
  "salary": "decimal (positive, required)",
  "hireDate": "date (not future, required)",
  "status": "enum (required)"
}
```

### Position Enum
- `INTERN`
- `JUNIOR_DEVELOPER`
- `SENIOR_DEVELOPER`
- `LEAD_DEVELOPER`
- `ARCHITECT`
- `MANAGER`
- `SENIOR_MANAGER`
- `DIRECTOR`
- `VP`
- `CEO`

### Employee Status Enum
- `ACTIVE`
- `INACTIVE`
- `ON_LEAVE`
- `TERMINATED`

### Department
```json
{
  "id": "UUID",
  "name": "string (max 100 chars, required, unique)",
  "description": "string (max 500 chars, optional)",
  "managerId": "UUID (optional)"
}
```

## Error Responses

### Validation Error (400 Bad Request)
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/employees",
  "errors": [
    {
      "field": "email",
      "message": "Email must be valid"
    }
  ]
}
```

### Not Found (404 Not Found)
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 123",
  "path": "/api/v1/employees/123"
}
```

### Internal Server Error (500)
```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred",
  "path": "/api/v1/employees"
}
```

## Sample Data

The application comes with pre-loaded sample data:

### Departments
- **Engineering**: Software development and technical operations
- **Human Resources**: Employee management and organizational development
- **Marketing**: Brand management and customer acquisition

### Employees
- **John Doe**: Senior Developer in Engineering ($85,000)
- **Jane Smith**: Manager in Human Resources ($95,000)
- **Mike Johnson**: Junior Developer in Engineering ($65,000)

## Interactive Documentation

Visit `http://localhost:8080/swagger-ui.html` for interactive API documentation with Swagger UI.

## Health Checks

- **Health**: `GET /actuator/health`
- **Metrics**: `GET /actuator/metrics`
- **Prometheus**: `GET /actuator/prometheus`