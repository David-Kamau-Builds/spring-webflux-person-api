package com.start.demo.service;

import com.start.demo.audit.AuditService;
import com.start.demo.dto.EmployeeResponse;
import com.start.demo.dto.PagedResponse;
import com.start.demo.model.Employee;
import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import com.start.demo.repository.DepartmentRepository;
import com.start.demo.repository.EmployeeRepository;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.UUID;

@Service
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final AuditService auditService;
    private final AtomicLong employeeCreatedCounter;
    private final AtomicLong employeeSearchCounter;
    private final CacheService cacheService;
    
    public EmployeeService(EmployeeRepository employeeRepository, 
                          DepartmentRepository departmentRepository,
                          AuditService auditService,
                          AtomicLong employeeCreatedCounter,
                          AtomicLong employeeSearchCounter,
                          CacheService cacheService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.auditService = auditService;
        this.employeeCreatedCounter = employeeCreatedCounter;
        this.employeeSearchCounter = employeeSearchCounter;
        this.cacheService = cacheService;
    }
    
    public Mono<Employee> createEmployee(Employee employee) {
        Employee newEmployee = new Employee(
            UUID.randomUUID(),
            employee.firstName(),
            employee.lastName(),
            employee.email(),
            employee.phone(),
            employee.departmentId(),
            employee.position(),
            employee.salary(),
            employee.hireDate(),
            employee.status()
        );
        return employeeRepository.save(newEmployee)
            .doOnSuccess(saved -> {
                employeeCreatedCounter.incrementAndGet();
                auditService.logEmployeeCreated(saved.id(), "admin").subscribe();
            });
    }
    
    public Flux<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
            .flatMap(this::mapToEmployeeResponse);
    }
    
    public Mono<EmployeeResponse> getEmployeeById(UUID id) {
        return employeeRepository.findById(id)
            .flatMap(this::mapToEmployeeResponse);
    }
    
    public Mono<Employee> updateEmployee(UUID id, Employee employee) {
        Employee updatedEmployee = new Employee(
            id,
            employee.firstName(),
            employee.lastName(),
            employee.email(),
            employee.phone(),
            employee.departmentId(),
            employee.position(),
            employee.salary(),
            employee.hireDate(),
            employee.status()
        );
        return employeeRepository.save(updatedEmployee);
    }
    
    public Mono<Void> deleteEmployee(UUID id) {
        return employeeRepository.deleteById(id);
    }
    
    public Flux<EmployeeResponse> searchEmployees(String name) {
        return employeeRepository.findByNameContaining(name)
            .flatMap(this::mapToEmployeeResponse)
            .doOnComplete(() -> {
                employeeSearchCounter.incrementAndGet();
                auditService.logEmployeeSearch(name, "admin").subscribe();
            });
    }
    
    public Flux<EmployeeResponse> getEmployeesByDepartment(UUID departmentId) {
        return employeeRepository.findByDepartmentId(departmentId)
            .flatMap(this::mapToEmployeeResponse);
    }
    
    public Flux<EmployeeResponse> getEmployeesByStatus(EmployeeStatus status) {
        return employeeRepository.findByStatus(status)
            .flatMap(this::mapToEmployeeResponse);
    }
    
    public Flux<EmployeeResponse> getEmployeesBySalaryRange(BigDecimal minSalary, BigDecimal maxSalary) {
        return employeeRepository.findBySalaryRange(minSalary, maxSalary)
            .flatMap(this::mapToEmployeeResponse);
    }
    
    public Mono<Long> getEmployeeCountByDepartment(UUID departmentId) {
        return employeeRepository.countByDepartmentId(departmentId);
    }
    
    public Mono<BigDecimal> getAverageSalaryByDepartment(UUID departmentId) {
        return employeeRepository.getAverageSalaryByDepartment(departmentId);
    }
    
    private Mono<EmployeeResponse> mapToEmployeeResponse(Employee employee) {
        return departmentRepository.findById(employee.departmentId())
            .map(department -> new EmployeeResponse(
                employee.id(),
                employee.firstName(),
                employee.lastName(),
                employee.getFullName(),
                employee.email(),
                employee.phone(),
                department.name(),
                employee.position(),
                employee.salary(),
                employee.hireDate(),
                employee.status()
            ))
            .defaultIfEmpty(new EmployeeResponse(
                employee.id(),
                employee.firstName(),
                employee.lastName(),
                employee.getFullName(),
                employee.email(),
                employee.phone(),
                "Unknown Department",
                employee.position(),
                employee.salary(),
                employee.hireDate(),
                employee.status()
            ));
    }
}