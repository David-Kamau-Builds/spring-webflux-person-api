package com.start.demo.repository;

import com.start.demo.dto.EmployeeResponse;
import com.start.demo.model.Employee;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.util.UUID;

public interface EmployeeRepository extends R2dbcRepository<Employee, UUID> {

    Flux<Employee> findByDepartmentId(UUID departmentId);

    Flux<Employee> findByStatusId(UUID statusId);

    @Query("SELECT * FROM employees WHERE LOWER(first_name) LIKE LOWER(CONCAT('%', :name, '%')) OR LOWER(last_name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Flux<Employee> findByNameContaining(@Param("name") String name);

    @Query("SELECT * FROM employees WHERE salary BETWEEN :minSalary AND :maxSalary")
    Flux<Employee> findBySalaryRange(@Param("minSalary") BigDecimal minSalary, @Param("maxSalary") BigDecimal maxSalary);

    @Query("SELECT COUNT(*) FROM employees WHERE department_id = :departmentId")
    Mono<Long> countByDepartmentId(@Param("departmentId") UUID departmentId);

    @Query("SELECT AVG(salary) FROM employees WHERE department_id = :departmentId")
    Mono<BigDecimal> getAverageSalaryByDepartment(@Param("departmentId") UUID departmentId);
}