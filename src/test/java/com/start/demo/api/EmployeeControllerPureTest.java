package com.start.demo.api;

import com.start.demo.model.Department;
import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import com.start.demo.repository.DepartmentRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import com.start.demo.repository.PositionRepository;
import com.start.demo.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class EmployeeControllerPureTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;
    @Mock
    private DepartmentRepository departmentRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private EmployeeStatusRepository employeeStatusRepository;

    private Employee testEmployee;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employeeId = UUID.randomUUID();
        testEmployee = new Employee(
            employeeId, "John", "Doe", "john.doe@test.com", "+1234567890",
            UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("75000"),
            LocalDate.now(), UUID.randomUUID()
        );
    }

    @Test
    void createEmployee_Success() {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(Mono.just(testEmployee));

        StepVerifier.create(employeeController.createEmployee(testEmployee))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void getAllEmployees_Success() {
        when(employeeService.getAllEmployees()).thenReturn(Flux.just(testEmployee));
        when(departmentRepository.findById(testEmployee.departmentId()))
            .thenReturn(Mono.just(new Department(testEmployee.departmentId(), "Engineering", "Eng Dept", null)));
        when(positionRepository.findById(testEmployee.positionId()))
            .thenReturn(Mono.just(new Position(testEmployee.positionId(), "Developer", "Software Developer")));
        when(employeeStatusRepository.findById(testEmployee.statusId()))
            .thenReturn(Mono.just(new EmployeeStatus(testEmployee.statusId(), "ACTIVE", "Active Status")));

        StepVerifier.create(employeeController.getAllEmployees())
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeService.getEmployeeById(employeeId)).thenReturn(Mono.just(testEmployee));
        when(departmentRepository.findById(testEmployee.departmentId()))
            .thenReturn(Mono.just(new Department(testEmployee.departmentId(), "Engineering", "Eng Dept", null)));
        when(positionRepository.findById(testEmployee.positionId()))
            .thenReturn(Mono.just(new Position(testEmployee.positionId(), "Developer", "Software Developer")));
        when(employeeStatusRepository.findById(testEmployee.statusId()))
            .thenReturn(Mono.just(new EmployeeStatus(testEmployee.statusId(), "ACTIVE", "Active Status")));

        StepVerifier.create(employeeController.getEmployeeById(employeeId))
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    void updateEmployee_Success() {
        when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(Mono.just(testEmployee));

        StepVerifier.create(employeeController.updateEmployee(employeeId, testEmployee))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeService.deleteEmployee(employeeId)).thenReturn(Mono.empty());

        StepVerifier.create(employeeController.deleteEmployee(employeeId))
            .verifyComplete();
    }

    @Test
    void getEmployeeCountByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        when(employeeService.getEmployeeCountByDepartment(departmentId)).thenReturn(Mono.just(5L));

        StepVerifier.create(employeeController.getEmployeeCountByDepartment(departmentId))
            .expectNext(5L)
            .verifyComplete();
    }

    @Test
    void getAverageSalaryByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        BigDecimal avgSalary = new BigDecimal("75000.00");
        when(employeeService.getAverageSalaryByDepartment(departmentId)).thenReturn(Mono.just(avgSalary));

        StepVerifier.create(employeeController.getAverageSalaryByDepartment(departmentId))
            .expectNext(avgSalary)
            .verifyComplete();
    }
}