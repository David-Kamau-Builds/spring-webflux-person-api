package com.start.demo.api;

import com.start.demo.model.Department;
import com.start.demo.service.DepartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class DepartmentControllerPureTest {

    @InjectMocks
    private DepartmentController departmentController;

    @Mock
    private DepartmentService departmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllDepartments() {
        Department department1 = new Department(UUID.randomUUID(), "Engineering", "Engineering department", null);
        Department department2 = new Department(UUID.randomUUID(), "HR", "HR department", null);
        List<Department> departments = Arrays.asList(department1, department2);

        when(departmentService.getAllDepartments()).thenReturn(Flux.fromIterable(departments));

        Flux<Department> result = departmentController.getAllDepartments();

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getDepartmentById() {
        UUID departmentId = UUID.randomUUID();
        Department department = new Department(departmentId, "Engineering", "Engineering department", null);

        when(departmentService.getDepartmentById(departmentId)).thenReturn(Mono.just(department));

        Mono<Department> result = departmentController.getDepartmentById(departmentId);

        StepVerifier.create(result)
                .expectNext(department)
                .verifyComplete();
    }

    @Test
    public void createDepartment() {
        Department department = new Department(UUID.randomUUID(), "Engineering", "Engineering department", null);

        when(departmentService.createDepartment(any(Department.class))).thenReturn(Mono.just(department));

        Mono<Department> result = departmentController.createDepartment(department);

        StepVerifier.create(result)
                .expectNext(department)
                .verifyComplete();
    }

    @Test
    public void updateDepartment() {
        UUID departmentId = UUID.randomUUID();
        Department department = new Department(departmentId, "Engineering", "Engineering department", null);

        when(departmentService.updateDepartment(eq(departmentId), any(Department.class))).thenReturn(Mono.just(department));

        Mono<Department> result = departmentController.updateDepartment(departmentId, department);

        StepVerifier.create(result)
                .expectNext(department)
                .verifyComplete();
    }

    @Test
    public void deleteDepartment() {
        UUID departmentId = UUID.randomUUID();

        when(departmentService.deleteDepartment(departmentId)).thenReturn(Mono.empty());

        Mono<Void> result = departmentController.deleteDepartment(departmentId);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
