package com.start.demo.model;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Table("employees")
public record Employee(
    @Id UUID id,
    
    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    String firstName,
    
    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    String lastName,
    
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    String email,
    
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Phone must be valid")
    String phone,
    
    @NotNull(message = "Department ID is required")
    UUID departmentId,
    
    @NotNull(message = "Position is required")
    Position position,
    
    @NotNull(message = "Salary is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Salary must be positive")
    BigDecimal salary,
    
    @NotNull(message = "Hire date is required")
    @PastOrPresent(message = "Hire date cannot be in the future")
    LocalDate hireDate,
    
    @NotNull(message = "Status is required")
    EmployeeStatus status
) {
    public String getFullName() {
        return firstName + " " + lastName;
    }
}