package com.start.demo.api;

import com.start.demo.dto.EmployeeResponse;
import com.start.demo.model.Employee;
import com.start.demo.repository.DepartmentRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import com.start.demo.repository.PositionRepository;
import com.start.demo.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employees")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeStatusRepository employeeStatusRepository;

    public EmployeeController(EmployeeService employeeService, DepartmentRepository departmentRepository, PositionRepository positionRepository, EmployeeStatusRepository employeeStatusRepository) {
        this.employeeService = employeeService;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.employeeStatusRepository = employeeStatusRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new employee", description = "Creates a new employee record")
    public Mono<Employee> createEmployee(@Valid @RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    @Operation(summary = "Get all employees", description = "Retrieves all employee records with department information")
    public Flux<EmployeeResponse> getAllEmployees() {
        return employeeService.getAllEmployees().flatMap(this::mapToEmployeeResponse);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get employee by ID", description = "Retrieves a specific employee by their ID")
    public Mono<EmployeeResponse> getEmployeeById(@PathVariable UUID id) {
        return employeeService.getEmployeeById(id).flatMap(this::mapToEmployeeResponse);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update employee", description = "Updates an existing employee record")
    public Mono<Employee> updateEmployee(@PathVariable UUID id, @Valid @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete employee", description = "Deletes an employee record")
    public Mono<Void> deleteEmployee(@PathVariable UUID id) {
        return employeeService.deleteEmployee(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search employees", description = "Search employees by name")
    public Flux<EmployeeResponse> searchEmployees(
        @Parameter(description = "Name to search for") @RequestParam String name) {
        return employeeService.searchEmployees(name).flatMap(this::mapToEmployeeResponse);
    }

    @GetMapping("/department/{departmentId})")
    @Operation(summary = "Get employees by department", description = "Retrieves all employees in a specific department")
    public Flux<EmployeeResponse> getEmployeesByDepartment(@PathVariable UUID departmentId) {
        return employeeService.getEmployeesByDepartment(departmentId).flatMap(this::mapToEmployeeResponse);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get employees by status", description = "Retrieves all employees with a specific status")
    public Flux<EmployeeResponse> getEmployeesByStatus(@PathVariable String status) {
        return employeeService.getEmployeesByStatus(status).flatMap(this::mapToEmployeeResponse);
    }

    @GetMapping("/salary-range")
    @Operation(summary = "Get employees by salary range", description = "Retrieves employees within a salary range")
    public Flux<EmployeeResponse> getEmployeesBySalaryRange(
        @Parameter(description = "Minimum salary") @RequestParam BigDecimal minSalary,
        @Parameter(description = "Maximum salary") @RequestParam BigDecimal maxSalary) {
        return employeeService.getEmployeesBySalaryRange(minSalary, maxSalary).flatMap(this::mapToEmployeeResponse);
    }

    @GetMapping("/department/{departmentId}/count")
    @Operation(summary = "Get employee count by department", description = "Returns the number of employees in a department")
    public Mono<Long> getEmployeeCountByDepartment(@PathVariable UUID departmentId) {
        return employeeService.getEmployeeCountByDepartment(departmentId);
    }

    @GetMapping("/department/{departmentId}/average-salary")
    @Operation(summary = "Get average salary by department", description = "Returns the average salary for a department")
    public Mono<BigDecimal> getAverageSalaryByDepartment(@PathVariable UUID departmentId) {
        return employeeService.getAverageSalaryByDepartment(departmentId);
    }

    private Mono<EmployeeResponse> mapToEmployeeResponse(Employee employee) {
        Mono<String> departmentName = departmentRepository.findById(employee.departmentId())
            .map(d -> d.name())
            .defaultIfEmpty("Unknown");
        Mono<String> positionName = positionRepository.findById(employee.positionId())
            .map(p -> p.name())
            .defaultIfEmpty("Unknown");
        Mono<String> statusName = employeeStatusRepository.findById(employee.statusId())
            .map(s -> s.name())
            .defaultIfEmpty("Unknown");

        return Mono.zip(departmentName, positionName, statusName)
            .map(tuple -> new EmployeeResponse(
                employee.id(),
                employee.firstName(),
                employee.lastName(),
                employee.getFullName(),
                employee.email(),
                employee.phone(),
                tuple.getT1(),
                tuple.getT2(),
                employee.salary(),
                employee.hireDate(),
                tuple.getT3()
            ));
    }
}