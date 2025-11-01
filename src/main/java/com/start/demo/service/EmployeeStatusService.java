
package com.start.demo.service;

import com.start.demo.model.EmployeeStatus;
import com.start.demo.repository.EmployeeStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class EmployeeStatusService {

    private final EmployeeStatusRepository employeeStatusRepository;

    @Autowired
    public EmployeeStatusService(EmployeeStatusRepository employeeStatusRepository) {
        this.employeeStatusRepository = employeeStatusRepository;
    }

    public Flux<EmployeeStatus> getAllStatuses() {
        return employeeStatusRepository.findAll();
    }
}
