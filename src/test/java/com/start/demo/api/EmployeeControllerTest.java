package com.start.demo.api;

import com.start.demo.dto.EmployeeResponse;
import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import com.start.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;

    @Test
    void shouldCreateEmployee() {
        Employee employee = createTestEmployee();
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(Mono.just(employee));

        webTestClient.post()
                .uri("/api/v1/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(employee)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(Employee.class)
                .value(result -> {
                    assertThat(result.firstName()).isEqualTo("John");
                    assertThat(result.lastName()).isEqualTo("Doe");
                });
    }

    @Test
    void shouldGetAllEmployees() {
        EmployeeResponse response = createTestEmployeeResponse();
        when(employeeService.getAllEmployees()).thenReturn(Flux.just(response));

        webTestClient.get()
                .uri("/api/v1/employees")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(EmployeeResponse.class)
                .hasSize(1);
    }

    @Test
    void shouldGetEmployeeById() {
        UUID id = UUID.randomUUID();
        EmployeeResponse response = createTestEmployeeResponse();
        when(employeeService.getEmployeeById(id)).thenReturn(Mono.just(response));

        webTestClient.get()
                .uri("/api/v1/employees/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(EmployeeResponse.class)
                .value(result -> assertThat(result.firstName()).isEqualTo("John"));
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

    private EmployeeResponse createTestEmployeeResponse() {
        return new EmployeeResponse(
                UUID.randomUUID(),
                "John",
                "Doe",
                "John Doe",
                "john.doe@company.com",
                "+1234567890",
                "Engineering",
                Position.SENIOR_DEVELOPER,
                new BigDecimal("85000.00"),
                LocalDate.now(),
                EmployeeStatus.ACTIVE
        );
    }
}