package com.start.demo.repository;

import com.start.demo.model.Department;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface DepartmentRepository extends R2dbcRepository<Department, UUID> {
    
    Mono<Department> findByName(String name);
    
    @Query("SELECT * FROM departments WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Flux<Department> findByNameContaining(@Param("name") String name);
    
    Flux<Department> findByManagerId(UUID managerId);
}