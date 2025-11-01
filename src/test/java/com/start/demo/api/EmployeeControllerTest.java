package com.start.demo.api;

import com.start.demo.model.Employee;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class EmployeeControllerTest {

    private static final UUID TEST_POSITION_ID = UUID.fromString("770e8400-e29b-41d4-a716-446655440001");
    private static final UUID TEST_STATUS_ID = UUID.fromString("880e8400-e29b-41d4-a716-446655440001");

    @Test
    void shouldCreateEmployeeModel() {
        Employee employee = createTestEmployee();

        assertThat(employee.firstName()).isEqualTo("John");
        assertThat(employee.lastName()).isEqualTo("Doe");
        assertThat(employee.email()).isEqualTo("john.doe@company.com");
        assertThat(employee.positionId()).isEqualTo(TEST_POSITION_ID);
        assertThat(employee.statusId()).isEqualTo(TEST_STATUS_ID);
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
                TEST_POSITION_ID,
                new BigDecimal("85000.00"),
                LocalDate.now(),
                TEST_STATUS_ID
        );
    }
}