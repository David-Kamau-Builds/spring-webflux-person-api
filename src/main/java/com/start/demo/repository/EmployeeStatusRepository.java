package com.start.demo.repository;

import com.start.demo.model.EmployeeStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import reactor.core.publisher.Mono;

@Repository
public interface EmployeeStatusRepository extends ReactiveCrudRepository<EmployeeStatus, UUID> {
    Mono<EmployeeStatus> findByNameIgnoreCase(String name);
}
