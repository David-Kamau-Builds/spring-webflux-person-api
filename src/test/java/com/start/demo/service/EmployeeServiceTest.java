package com.start.demo.service;

import com.start.demo.audit.AuditService;
import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.repository.EmployeeRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import com.start.demo.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {

    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private PositionRepository positionRepository;
    @Mock
    private EmployeeStatusRepository employeeStatusRepository;
    @Mock
    private AuditService auditService;
    @Mock
    private AtomicLong employeeCreatedCounter;
    @Mock
    private AtomicLong employeeSearchCounter;

    private Employee testEmployee;
    private UUID employeeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(
            employeeRepository, positionRepository, employeeStatusRepository,
            auditService, employeeCreatedCounter, employeeSearchCounter
        );

        employeeId = UUID.randomUUID();
        testEmployee = new Employee(
            employeeId, "John", "Doe", "john.doe@test.com", "+1234567890",
            UUID.randomUUID(), UUID.randomUUID(), new BigDecimal("75000"),
            LocalDate.now(), UUID.randomUUID()
        );
    }

    @Test
    void createEmployee_Success() {
        Employee savedEmployee = new Employee(employeeId, testEmployee.firstName(),
            testEmployee.lastName(), testEmployee.email(), testEmployee.phone(),
            testEmployee.departmentId(), testEmployee.positionId(), testEmployee.salary(),
            testEmployee.hireDate(), testEmployee.statusId());

        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.just(savedEmployee));
        when(auditService.logEmployeeCreated(any(UUID.class), eq("admin"))).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.createEmployee(testEmployee))
            .expectNext(savedEmployee)
            .verifyComplete();

        verify(employeeCreatedCounter).incrementAndGet();
        verify(auditService).logEmployeeCreated(employeeId, "admin");
    }

    @Test
    void getAllEmployees_Success() {
        when(employeeRepository.findAll()).thenReturn(Flux.just(testEmployee));

        StepVerifier.create(employeeService.getAllEmployees())
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(employeeId)).thenReturn(Mono.just(testEmployee));

        StepVerifier.create(employeeService.getEmployeeById(employeeId))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void updateEmployee_Success() {
        Employee updatedEmployee = new Employee(employeeId, "Jane", "Smith",
            "jane.smith@test.com", "+9876543210", testEmployee.departmentId(),
            testEmployee.positionId(), new BigDecimal("80000"), testEmployee.hireDate(),
            testEmployee.statusId());

        when(employeeRepository.findById(employeeId)).thenReturn(Mono.just(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(Mono.just(updatedEmployee));

        StepVerifier.create(employeeService.updateEmployee(employeeId, updatedEmployee))
            .expectNext(updatedEmployee)
            .verifyComplete();
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.deleteById(employeeId)).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.deleteEmployee(employeeId))
            .verifyComplete();
    }

    @Test
    void searchEmployees_Success() {
        String searchName = "John";
        when(employeeRepository.findByNameContaining(searchName)).thenReturn(Flux.just(testEmployee));
        when(auditService.logEmployeeSearch(searchName, "admin")).thenReturn(Mono.empty());

        StepVerifier.create(employeeService.searchEmployees(searchName))
            .expectNext(testEmployee)
            .verifyComplete();

        verify(employeeSearchCounter).incrementAndGet();
        verify(auditService).logEmployeeSearch(searchName, "admin");
    }

    @Test
    void getEmployeesByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        when(employeeRepository.findByDepartmentId(departmentId)).thenReturn(Flux.just(testEmployee));

        StepVerifier.create(employeeService.getEmployeesByDepartment(departmentId))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void getEmployeesByStatus_Success() {
        String statusName = "ACTIVE";
        UUID statusId = UUID.randomUUID();
        EmployeeStatus status = new EmployeeStatus(statusId, statusName, "Active employee");

        when(employeeStatusRepository.findByNameIgnoreCase(statusName)).thenReturn(Mono.just(status));
        when(employeeRepository.findByStatusId(statusId)).thenReturn(Flux.just(testEmployee));

        StepVerifier.create(employeeService.getEmployeesByStatus(statusName))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void getEmployeesBySalaryRange_Success() {
        BigDecimal minSalary = new BigDecimal("50000");
        BigDecimal maxSalary = new BigDecimal("100000");

        when(employeeRepository.findBySalaryRange(minSalary, maxSalary)).thenReturn(Flux.just(testEmployee));

        StepVerifier.create(employeeService.getEmployeesBySalaryRange(minSalary, maxSalary))
            .expectNext(testEmployee)
            .verifyComplete();
    }

    @Test
    void getEmployeeCountByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        Long expectedCount = 5L;

        when(employeeRepository.countByDepartmentId(departmentId)).thenReturn(Mono.just(expectedCount));

        StepVerifier.create(employeeService.getEmployeeCountByDepartment(departmentId))
            .expectNext(expectedCount)
            .verifyComplete();
    }

    @Test
    void getAverageSalaryByDepartment_Success() {
        UUID departmentId = UUID.randomUUID();
        BigDecimal expectedAverage = new BigDecimal("75000.00");

        when(employeeRepository.getAverageSalaryByDepartment(departmentId)).thenReturn(Mono.just(expectedAverage));

        StepVerifier.create(employeeService.getAverageSalaryByDepartment(departmentId))
            .expectNext(expectedAverage)
            .verifyComplete();
    }
}