package com.start.demo.api;

import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerTest {

    @Test
    void shouldCreateEmployeeModel() {
        Employee employee = createTestEmployee();
        
        assertThat(employee.firstName()).isEqualTo("John");
        assertThat(employee.lastName()).isEqualTo("Doe");
        assertThat(employee.email()).isEqualTo("john.doe@company.com");
        assertThat(employee.position()).isEqualTo(Position.SENIOR_DEVELOPER);
        assertThat(employee.status()).isEqualTo(EmployeeStatus.ACTIVE);
    }

    @Test
    void shouldGetFullName() {
        Employee employee = createTestEmployee();
        
        assertThat(employee.getFullName()).isEqualTo("John Doe");
    }

    private Employee createTestEmployee() {
        return new Employee(
                UUID.randomUUID(),
                "John",
                "Doe",
                "john.doe@company.com",
                "+1234567890",
                UUID.randomUUID(),
                Position.SENIOR_DEVELOPER,
                new BigDecimal("85000.00"),
                LocalDate.now(),
                EmployeeStatus.ACTIVE
        );
    }
}