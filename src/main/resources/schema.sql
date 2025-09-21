-- Departments table
CREATE TABLE IF NOT EXISTS departments (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(500),
    manager_id UUID
);

-- Employees table
CREATE TABLE IF NOT EXISTS employees (
    id UUID PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    department_id UUID NOT NULL,
    position VARCHAR(50) NOT NULL,
    salary DECIMAL(10,2) NOT NULL,
    hire_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Sample data
INSERT INTO departments (id, name, description) VALUES 
('550e8400-e29b-41d4-a716-446655440001', 'Engineering', 'Software development and technical operations'),
('550e8400-e29b-41d4-a716-446655440002', 'Human Resources', 'Employee management and organizational development'),
('550e8400-e29b-41d4-a716-446655440003', 'Marketing', 'Brand management and customer acquisition');

INSERT INTO employees (id, first_name, last_name, email, phone, department_id, position, salary, hire_date, status) VALUES 
('660e8400-e29b-41d4-a716-446655440001', 'John', 'Doe', 'john.doe@company.com', '+1234567890', '550e8400-e29b-41d4-a716-446655440001', 'SENIOR_DEVELOPER', 85000.00, '2023-01-15', 'ACTIVE'),
('660e8400-e29b-41d4-a716-446655440002', 'Jane', 'Smith', 'jane.smith@company.com', '+1234567891', '550e8400-e29b-41d4-a716-446655440002', 'MANAGER', 95000.00, '2022-03-10', 'ACTIVE'),
('660e8400-e29b-41d4-a716-446655440003', 'Mike', 'Johnson', 'mike.johnson@company.com', '+1234567892', '550e8400-e29b-41d4-a716-446655440001', 'JUNIOR_DEVELOPER', 65000.00, '2023-06-01', 'ACTIVE');