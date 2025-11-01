-- Departments table
CREATE TABLE departments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500),
    manager_id UUID
);

CREATE TABLE positions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    description VARCHAR(500)
);

CREATE TABLE employee_statuses (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE employees (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department_id UUID NOT NULL,
    position_id UUID NOT NULL,
    status_id UUID NOT NULL,
    salary DECIMAL(12, 2) NOT NULL,
    hire_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),

    -- CONSTRAINTS
    FOREIGN KEY (department_id) REFERENCES departments(id),
    FOREIGN KEY (position_id) REFERENCES positions(id),
    FOREIGN KEY (status_id) REFERENCES employee_statuses(id),
    CHECK (salary > 0),
    CHECK (hire_date <= CURRENT_DATE)
);

CREATE TABLE IF NOT EXISTS employee_audit (
    audit_id SERIAL PRIMARY KEY,
    employee_id UUID NOT NULL,
    changed_at TIMESTAMP NOT NULL DEFAULT NOW(),
    old_salary DECIMAL(12, 2),
    new_salary DECIMAL(12, 2),
    old_position_id UUID,
    new_position_id UUID,
    change_type VARCHAR(10) NOT NULL -- e.g., 'UPDATE', 'DELETE'
);

-- Indexes for performance on frequently queried columns
CREATE INDEX IF NOT EXISTS idx_employees_department_id ON employees(department_id);
CREATE INDEX IF NOT EXISTS idx_employees_position_id ON employees(position_id);
CREATE INDEX IF NOT EXISTS idx_employees_last_name ON employees(last_name);


-- View for simplified reporting on employee details
CREATE OR REPLACE VIEW v_employee_details AS
SELECT
    e.id AS employee_id,
    e.first_name,
    e.last_name,
    e.email,
    d.name AS department_name,
    p.name AS position_name,
    s.name AS status,
    e.salary,
    e.hire_date
FROM
    employees e
LEFT JOIN departments d ON e.department_id = d.id
LEFT JOIN positions p ON e.position_id = p.id
LEFT JOIN employee_statuses s ON e.status_id = s.id;

-- # 5. SAMPLE DATA

-- Departments
INSERT INTO departments (id, name, description) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Engineering', 'Software development and technical operations'),
('550e8400-e29b-41d4-a716-446655440002', 'Human Resources', 'Employee management and organizational development'),
('550e8400-e29b-41d4-a716-446655440003', 'Marketing', 'Brand management and customer acquisition'),
('550e8400-e29b-41d4-a716-446655440004', 'Sales', 'Customer outreach, deal closure, and revenue generation'),
('550e8400-e29b-41d4-a716-446655440005', 'Finance', 'Budgeting, accounting, and financial planning'),
('550e8400-e29b-41d4-a716-446655440006', 'Product Management', 'Product strategy, roadmap, and cross-functional coordination');

-- Positions
INSERT INTO positions (id, name, description) VALUES
('770e8400-e29b-41d4-a716-446655440001', 'Senior Developer', 'Builds and maintains complex software systems.'),
('770e8400-e29b-41d4-a716-446655440002', 'Junior Developer', 'Assists in the development of software applications.'),
('770e8400-e29b-41d4-a716-446655440003', 'Manager', 'Manages a team or department.'),
('770e8400-e29b-41d4-a716-446655440004', 'HR Specialist', 'Handles recruitment, onboarding, and employee relations.'),
('770e8400-e29b-41d4-a716-446655440005', 'Marketing Coordinator', 'Supports campaigns, content, and market research.'),
('770e8400-e29b-41d4-a716-446655440006', 'Sales Representative', 'Engages prospects and closes sales deals.'),
('770e8400-e29b-41d4-a716-446655440007', 'Accountant', 'Manages financial records and compliance.'),
('770e8400-e29b-41d4-a716-446655440008', 'Product Manager', 'Defines product vision and drives development lifecycle.');

-- Statuses
INSERT INTO employee_statuses (id, name, description) VALUES
('880e8400-e29b-41d4-a716-446655440001', 'Active', 'Currently employed and working.'),
('880e8400-e29b-41d4-a716-446655440002', 'On Leave', 'Temporarily absent from work.'),
('880e8400-e29b-41d4-a716-446655440003', 'Terminated', 'No longer with the company.'),
('880e8400-e29b-41d4-a716-446655440004', 'Probation', 'New hire under evaluation period.'),
('880e8400-e29b-41d4-a716-446655440005', 'Remote', 'Full-time remote worker.');

-- Employees
INSERT INTO employees (id, first_name, last_name, email, phone, department_id, position_id, status_id, salary, hire_date) VALUES
('660e8400-e29b-41d4-a716-446655440001', 'John', 'Doe', 'john.doe@company.com', '+1234567890', '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440001', 85000.00, '2023-01-15'),
('660e8400-e29b-41d4-a716-446655440002', 'Jane', 'Smith', 'jane.smith@company.com', '+1234567891', '550e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440003', '880e8400-e29b-41d4-a716-446655440001', 95000.00, '2022-03-10'),
('660e8400-e29b-41d4-a716-446655440003', 'Mike', 'Johnson', 'mike.johnson@company.com', '+1234567892', '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440001', 65000.00, '2023-06-01'),
('660e8400-e29b-41d4-a716-446655440004', 'Emily', 'Davis', 'emily.davis@company.com', '+1234567893', '550e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440005', '880e8400-e29b-41d4-a716-446655440001', 58000.00, '2023-09-20'),
('660e8400-e29b-41d4-a716-446655440005', 'Alex', 'Brown', 'alex.brown@company.com', '+1234567894', '550e8400-e29b-41d4-a716-446655440004', '770e8400-e29b-41d4-a716-446655440006', '880e8400-e29b-41d4-a716-446655440001', 70000.00, '2022-11-05'),
('660e8400-e29b-41d4-a716-446655440006', 'Sarah', 'Wilson', 'sarah.wilson@company.com', '+1234567895', '550e8400-e29b-41d4-a716-446655440005', '770e8400-e29b-41d4-a716-446655440007', '880e8400-e29b-41d4-a716-446655440001', 72000.00, '2021-07-12'),
('660e8400-e29b-41d4-a716-446655440007', 'David', 'Lee', 'david.lee@company.com', '+1234567896', '550e8400-e29b-41d4-a716-446655440006', '770e8400-e29b-41d4-a716-446655440008', '880e8400-e29b-41d4-a716-446655440001', 110000.00, '2022-05-18'),
('660e8400-e29b-41d4-a716-446655440008', 'Lisa', 'Martinez', 'lisa.martinez@company.com', '+1234567897', '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440004', 60000.00, '2024-02-28'),
('660e8400-e29b-41d4-a716-446655440009', 'Tom', 'Garcia', 'tom.garcia@company.com', '+1234567898', '550e8400-e29b-41d4-a716-446655440002', '770e8400-e29b-41d4-a716-446655440004', '880e8400-e29b-41d4-a716-446655440001', 68000.00, '2023-04-10'),
('660e8400-e29b-41d4-a716-446655440010', 'Nina', 'Patel', 'nina.patel@company.com', '+1234567899', '550e8400-e29b-41d4-a716-446655440003', '770e8400-e29b-41d4-a716-446655440003', '880e8400-e29b-41d4-a716-446655440001', 90000.00, '2021-10-03'),
('660e8400-e29b-41d4-a716-446655440011', 'Omar', 'Khan', 'omar.khan@company.com', '+1234567800', '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440001', '880e8400-e29b-41d4-a716-446655440005', 88000.00, '2023-08-15'),
('660e8400-e29b-41d4-a716-446655440012', 'Rachel', 'Green', 'rachel.green@company.com', '+1234567801', '550e8400-e29b-41d4-a716-446655440004', '770e8400-e29b-41d4-a716-446655440006', '880e8400-e29b-41d4-a716-446655440002', 71000.00, '2022-12-01'),
('660e8400-e29b-41d4-a716-446655440013', 'Chris', 'Evans', 'chris.evans@company.com', '+1234567802', '550e8400-e29b-41d4-a716-446655440006', '770e8400-e29b-41d4-a716-446655440008', '880e8400-e29b-41d4-a716-446655440001', 115000.00, '2020-06-20'),
('660e8400-e29b-41d4-a716-446655440014', 'Sofia', 'Lopez', 'sofia.lopez@company.com', '+1234567803', '550e8400-e29b-41d4-a716-446655440005', '770e8400-e29b-41d4-a716-446655440007', '880e8400-e29b-41d4-a716-446655440003', 65000.00, '2021-11-11'),
('660e8400-e29b-41d4-a716-446655440015', 'Liam', 'Nguyen', 'liam.nguyen@company.com', '+1234567804', '550e8400-e29b-41d4-a716-446655440001', '770e8400-e29b-41d4-a716-446655440002', '880e8400-e29b-41d4-a716-446655440001', 62000.00, '2024-01-10');