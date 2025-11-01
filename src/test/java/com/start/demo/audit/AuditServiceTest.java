package com.start.demo.audit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class AuditServiceTest {

    private AuditService auditService;

    @BeforeEach
    void setUp() {
        auditService = new AuditService();
    }

    @Test
    void logEmployeeCreated_Success() {
        UUID employeeId = UUID.randomUUID();
        String createdBy = "admin";

        StepVerifier.create(auditService.logEmployeeCreated(employeeId, createdBy))
            .verifyComplete();
    }

    @Test
    void logEmployeeUpdated_Success() {
        UUID employeeId = UUID.randomUUID();
        String updatedBy = "admin";

        StepVerifier.create(auditService.logEmployeeUpdated(employeeId, updatedBy))
            .verifyComplete();
    }

    @Test
    void logEmployeeDeleted_Success() {
        UUID employeeId = UUID.randomUUID();
        String deletedBy = "admin";

        StepVerifier.create(auditService.logEmployeeDeleted(employeeId, deletedBy))
            .verifyComplete();
    }

    @Test
    void logEmployeeSearch_Success() {
        String searchTerm = "John";
        String searchedBy = "admin";

        StepVerifier.create(auditService.logEmployeeSearch(searchTerm, searchedBy))
            .verifyComplete();
    }
}