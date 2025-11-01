package com.start.demo.service;

import com.start.demo.model.Department;
import com.start.demo.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class DepartmentServiceTest {

    private DepartmentService departmentService;

    @Mock
    private DepartmentRepository departmentRepository;

    private Department testDepartment;
    private UUID departmentId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        departmentService = new DepartmentService(departmentRepository);

        departmentId = UUID.randomUUID();
        testDepartment = new Department(departmentId, "Engineering", "Software Engineering Department", null);
    }

    @Test
    void createDepartment_Success() {
        Department savedDepartment = new Department(departmentId, testDepartment.name(),
            testDepartment.description(), testDepartment.managerId());

        when(departmentRepository.save(any(Department.class))).thenReturn(Mono.just(savedDepartment));

        StepVerifier.create(departmentService.createDepartment(testDepartment))
            .expectNext(savedDepartment)
            .verifyComplete();
    }

    @Test
    void getAllDepartments_Success() {
        when(departmentRepository.findAll()).thenReturn(Flux.just(testDepartment));

        StepVerifier.create(departmentService.getAllDepartments())
            .expectNext(testDepartment)
            .verifyComplete();
    }

    @Test
    void getDepartmentById_Success() {
        when(departmentRepository.findById(departmentId)).thenReturn(Mono.just(testDepartment));

        StepVerifier.create(departmentService.getDepartmentById(departmentId))
            .expectNext(testDepartment)
            .verifyComplete();
    }

    @Test
    void updateDepartment_Success() {
        Department updatedDepartment = new Department(departmentId, "Updated Engineering",
            "Updated description", UUID.randomUUID());

        when(departmentRepository.save(any(Department.class))).thenReturn(Mono.just(updatedDepartment));

        StepVerifier.create(departmentService.updateDepartment(departmentId, updatedDepartment))
            .expectNext(updatedDepartment)
            .verifyComplete();
    }

    @Test
    void deleteDepartment_Success() {
        when(departmentRepository.deleteById(departmentId)).thenReturn(Mono.empty());

        StepVerifier.create(departmentService.deleteDepartment(departmentId))
            .verifyComplete();
    }

    @Test
    void searchDepartments_Success() {
        String searchName = "Eng";
        when(departmentRepository.findByNameContaining(searchName)).thenReturn(Flux.just(testDepartment));

        StepVerifier.create(departmentService.searchDepartments(searchName))
            .expectNext(testDepartment)
            .verifyComplete();
    }
}