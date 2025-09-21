package com.start.demo.api;

import com.start.demo.model.Department;
import com.start.demo.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@Tag(name = "Department Management", description = "APIs for managing departments")
public class DepartmentController {
    
    private final DepartmentService departmentService;
    
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new department", description = "Creates a new department")
    public Mono<Department> createDepartment(@Valid @RequestBody Department department) {
        return departmentService.createDepartment(department);
    }
    
    @GetMapping
    @Operation(summary = "Get all departments", description = "Retrieves all departments")
    public Flux<Department> getAllDepartments() {
        return departmentService.getAllDepartments();
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID", description = "Retrieves a specific department by ID")
    public Mono<Department> getDepartmentById(@PathVariable UUID id) {
        return departmentService.getDepartmentById(id);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update department", description = "Updates an existing department")
    public Mono<Department> updateDepartment(@PathVariable UUID id, @Valid @RequestBody Department department) {
        return departmentService.updateDepartment(id, department);
    }
    
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete department", description = "Deletes a department")
    public Mono<Void> deleteDepartment(@PathVariable UUID id) {
        return departmentService.deleteDepartment(id);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search departments", description = "Search departments by name")
    public Flux<Department> searchDepartments(
        @Parameter(description = "Name to search for") @RequestParam String name) {
        return departmentService.searchDepartments(name);
    }
}