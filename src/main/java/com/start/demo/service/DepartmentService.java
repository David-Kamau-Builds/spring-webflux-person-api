package com.start.demo.service;

import com.start.demo.model.Department;
import com.start.demo.repository.DepartmentRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;

@Service
public class DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    
    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
    
    public Mono<Department> createDepartment(Department department) {
        Department newDepartment = new Department(
            null, // Let the database generate the ID
            department.name(),
            department.description(),
            department.managerId()
        );
        return departmentRepository.save(newDepartment);
    }
    
    public Flux<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }
    
    public Mono<Department> getDepartmentById(UUID id) {
        return departmentRepository.findById(id);
    }
    
    public Mono<Department> updateDepartment(UUID id, Department department) {
        Department updatedDepartment = new Department(
            id,
            department.name(),
            department.description(),
            department.managerId()
        );
        return departmentRepository.save(updatedDepartment);
    }
    
    public Mono<Void> deleteDepartment(UUID id) {
        return departmentRepository.deleteById(id);
    }
    
    public Flux<Department> searchDepartments(String name) {
        return departmentRepository.findByNameContaining(name);
    }
}