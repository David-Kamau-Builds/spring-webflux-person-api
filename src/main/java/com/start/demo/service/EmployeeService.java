package com.start.demo.service;

import com.start.demo.audit.AuditService;
import com.start.demo.dto.EmployeeResponse;
import com.start.demo.model.Employee;
import com.start.demo.repository.EmployeeRepository;
import com.start.demo.repository.PositionRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeStatusRepository employeeStatusRepository; // Added
    private final AuditService auditService;
    private final AtomicLong employeeCreatedCounter;
    private final AtomicLong employeeSearchCounter;

    public EmployeeService(EmployeeRepository employeeRepository,
                           EmployeeStatusRepository employeeStatusRepository, // Added
                           AuditService auditService,
                           AtomicLong employeeCreatedCounter,
                           AtomicLong employeeSearchCounter) {
        this.employeeRepository = employeeRepository;
        this.employeeStatusRepository = employeeStatusRepository;
        this.auditService = auditService;
        this.employeeCreatedCounter = employeeCreatedCounter;
        this.employeeSearchCounter = employeeSearchCounter;
    }

    public Mono<Employee> createEmployee(Employee employee) {
        Employee newEmployee = new Employee(
            null, // Let the database generate the ID
            employee.firstName(),
            employee.lastName(),
            employee.email(),
            employee.phone(),
            employee.departmentId(),
            employee.positionId(), // Updated
            employee.salary(),
            employee.hireDate(),
            employee.statusId() // Updated
        );
        return employeeRepository.save(newEmployee)
            .doOnSuccess(saved -> {
                employeeCreatedCounter.incrementAndGet();
                auditService.logEmployeeCreated(saved.id(), "admin").subscribe();
            });
    }

    public Flux<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Mono<Employee> getEmployeeById(UUID id) {
        return employeeRepository.findById(id);
    }

    public Mono<Employee> updateEmployee(UUID id, Employee employee) {
        return employeeRepository.findById(id)
            .flatMap(existingEmployee -> {
                Employee updatedEmployee = new Employee(
                    id,
                    employee.firstName(),
                    employee.lastName(),
                    employee.email(),
                    employee.phone(),
                    employee.departmentId(),
                    employee.positionId(), // Updated
                    employee.salary(),
                    employee.hireDate(),
                    employee.statusId() // Updated
                );
                return employeeRepository.save(updatedEmployee);
            });
    }

    public Mono<Void> deleteEmployee(UUID id) {
        return employeeRepository.deleteById(id);
    }

    public Flux<Employee> searchEmployees(String name) { // Note: Returns Employee, not EmployeeResponse for simplicity here
        return employeeRepository.findByNameContaining(name)
            .doOnSubscribe(s -> {
                employeeSearchCounter.incrementAndGet();
                auditService.logEmployeeSearch(name, "admin").subscribe();
            });
    }

    public Flux<Employee> getEmployeesByDepartment(UUID departmentId) { // Note: Returns Employee
        return employeeRepository.findByDepartmentId(departmentId);
    }

    public Flux<Employee> getEmployeesByStatus(String statusName) { // Note: Returns Employee
        return employeeStatusRepository.findByNameIgnoreCase(statusName)
            .flatMapMany(status -> employeeRepository.findByStatusId(status.id()));
    }

    public Flux<Employee> getEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) { // Note: Returns Employee
        return employeeRepository.findBySalaryRange(minSalary, maxSalary);
    }

    public Mono<Long> getEmployeeCountByDepartment(UUID departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }

    public Mono<BigDecimal> getAverageSalaryByDepartment(UUID departmentId) {
        return employeeRepository.getAverageSalaryByDepartment(departmentId);
    }
}