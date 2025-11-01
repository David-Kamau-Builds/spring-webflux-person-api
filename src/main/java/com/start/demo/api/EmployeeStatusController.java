
package com.start.demo.api;

import com.start.demo.model.EmployeeStatus;
import com.start.demo.service.EmployeeStatusService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/statuses")
@Tag(name = "Employee Statuses", description = "APIs for retrieving employee statuses")
public class EmployeeStatusController {

    private final EmployeeStatusService employeeStatusService;

    @Autowired
    public EmployeeStatusController(EmployeeStatusService employeeStatusService) {
        this.employeeStatusService = employeeStatusService;
    }

    @GetMapping
    @Operation(summary = "Get all employee statuses", description = "Retrieves a list of all available employee statuses")
    public Flux<EmployeeStatus> getAllStatuses() {
        return employeeStatusService.getAllStatuses();
    }
}
