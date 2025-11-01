package com.start.demo.api;

import com.start.demo.TestConfig;
import com.start.demo.dto.EmployeeResponse;
import com.start.demo.model.Department;
import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import com.start.demo.repository.DepartmentRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import com.start.demo.repository.PositionRepository;
import com.start.demo.security.JwtUtil;
import com.start.demo.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = EmployeeController.class)
@WithMockUser
@Import({EmployeeController.class, TestConfig.class})
class EmployeeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private DepartmentRepository departmentRepository;
    @MockBean
    private PositionRepository positionRepository;
    @MockBean
    private EmployeeStatusRepository employeeStatusRepository;
    @MockBean
    private JwtUtil jwtUtil;

    @Test
    void createEmployee_Success() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(null, "John", "Doe", "john.doe@test.com", "+1234567890",
            UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("75000"), LocalDate.now(), UUID.randomUUID());
        Employee savedEmployee = new Employee(employeeId, employee.firstName(), employee.lastName(),
            employee.email(), employee.phone(), employee.departmentId(), employee.positionId(),
            employee.salary(), employee.hireDate(), employee.statusId());

        when(employeeService.createEmployee(any(Employee.class))).thenReturn(Mono.just(savedEmployee));

        webTestClient.post()
            .uri("/api/v1/employees")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(employee)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(Employee.class);
    }

    @Test
    void getAllEmployees_Success() {
        UUID employeeId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        UUID positionId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        Employee employee = new Employee(employeeId, "John", "Doe", "john.doe@test.com", "+1234567890",
            departmentId, positionId, new BigDecimal("75000"), LocalDate.now(), statusId);

        when(employeeService.getAllEmployees()).thenReturn(Flux.just(employee));
        when(departmentRepository.findById(departmentId)).thenReturn(Mono.just(new Department(departmentId, "Engineering", "Eng Dept", null)));
        when(positionRepository.findById(positionId)).thenReturn(Mono.just(new Position(positionId, "Developer", "Software Developer")));
        when(employeeStatusRepository.findById(statusId)).thenReturn(Mono.just(new EmployeeStatus(statusId, "ACTIVE", "Active Status")));

        webTestClient.get()
            .uri("/api/v1/employees")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeResponse.class)
            .hasSize(1);
    }

    @Test
    void getEmployeeById_Success() {
        UUID employeeId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        UUID positionId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        Employee employee = new Employee(employeeId, "John", "Doe", "john.doe@test.com", "+1234567890",
            departmentId, positionId, new BigDecimal("75000"), LocalDate.now(), statusId);

        when(employeeService.getEmployeeById(employeeId)).thenReturn(Mono.just(employee));
        when(departmentRepository.findById(departmentId)).thenReturn(Mono.just(new Department(departmentId, "Engineering", "Eng Dept", null)));
        when(positionRepository.findById(positionId)).thenReturn(Mono.just(new Position(positionId, "Developer", "Software Developer")));
        when(employeeStatusRepository.findById(statusId)).thenReturn(Mono.just(new EmployeeStatus(statusId, "ACTIVE", "Active Status")));

        webTestClient.get()
            .uri("/api/v1/employees/{id}", employeeId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(EmployeeResponse.class);
    }

    @Test
    void updateEmployee_Success() {
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(employeeId, "Jane", "Smith", "jane.smith@test.com", "+9876543210",
            UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("80000"), LocalDate.now(), UUID.randomUUID());

        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(Mono.just(employee));

        webTestClient.put()
            .uri("/api/v1/employees/{id}", employeeId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(employee)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Employee.class);
    }

    @Test
    void deleteEmployee_Success() {
        UUID employeeId = UUID.randomUUID();
        when(employeeService.deleteEmployee(employeeId)).thenReturn(Mono.empty());

        webTestClient.delete()
            .uri("/api/v1/employees/{id}", employeeId)
            .exchange()
            .expectStatus().isNoContent();
    }

    @Test
    void searchEmployees_Success() {
        UUID employeeId = UUID.randomUUID();
        UUID departmentId = UUID.randomUUID();
        UUID positionId = UUID.randomUUID();
        UUID statusId = UUID.randomUUID();

        Employee employee = new Employee(employeeId, "John", "Doe", "john.doe@test.com", "+1234567890",
            departmentId, positionId, new BigDecimal("75000"), LocalDate.now(), statusId);

        when(employeeService.searchEmployees("John")).thenReturn(Flux.just(employee));
        when(departmentRepository.findById(departmentId)).thenReturn(Mono.just(new Department(departmentId, "Engineering", "Eng Dept", null)));
        when(positionRepository.findById(positionId)).thenReturn(Mono.just(new Position(positionId, "Developer", "Software Developer")));
        when(employeeStatusRepository.findById(statusId)).thenReturn(Mono.just(new EmployeeStatus(statusId, "ACTIVE", "Active Status")));

        webTestClient.get()
            .uri("/api/v1/employees/search?name=John")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(EmployeeResponse.class)
            .hasSize(1);
    }

    @Test
    void getEmployeeCountByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        when(employeeService.getEmployeeCountByDepartment(departmentId)).thenReturn(Mono.just(5L));

        webTestClient.get()
            .uri("/api/v1/employees/department/{departmentId}/count", departmentId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Long.class)
            .isEqualTo(5L);
    }

    @Test
    void getAverageSalaryByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        BigDecimal avgSalary = new BigDecimal("75000.00");
        when(employeeService.getAverageSalaryByDepartment(departmentId)).thenReturn(Mono.just(avgSalary));

        webTestClient.get()
            .uri("/api/v1/employees/department/{departmentId}/average-salary", departmentId)
            .exchange()
            .expectStatus().isOk()
            .expectBody(BigDecimal.class)
            .isEqualTo(avgSalary);
    }
}