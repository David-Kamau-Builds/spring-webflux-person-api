package com.start.demo.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditService {
    
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");
    
    public Mono<Void> logEmployeeCreated(UUID employeeId, String createdBy) {
        return Mono.fromRunnable(() -> 
            auditLogger.info("EMPLOYEE_CREATED: id={}, createdBy={}, timestamp={}", 
                employeeId, createdBy, LocalDateTime.now())
        );
    }
    
    public Mono<Void> logEmployeeUpdated(UUID employeeId, String updatedBy) {
        return Mono.fromRunnable(() -> 
            auditLogger.info("EMPLOYEE_UPDATED: id={}, updatedBy={}, timestamp={}", 
                employeeId, updatedBy, LocalDateTime.now())
        );
    }
    
    public Mono<Void> logEmployeeDeleted(UUID employeeId, String deletedBy) {
        return Mono.fromRunnable(() -> 
            auditLogger.info("EMPLOYEE_DELETED: id={}, deletedBy={}, timestamp={}", 
                employeeId, deletedBy, LocalDateTime.now())
        );
    }
    
    public Mono<Void> logEmployeeSearch(String searchTerm, String searchedBy) {
        return Mono.fromRunnable(() -> 
            auditLogger.info("EMPLOYEE_SEARCH: term={}, searchedBy={}, timestamp={}", 
                searchTerm, searchedBy, LocalDateTime.now())
        );
    }
}