package com.start.demo.api;

import com.start.demo.model.EmployeeStatus;
import com.start.demo.service.EmployeeStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

class EmployeeStatusControllerTest {

    @InjectMocks
    private EmployeeStatusController employeeStatusController;

    @Mock
    private EmployeeStatusService employeeStatusService;

    private EmployeeStatus activeStatus;
    private EmployeeStatus inactiveStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activeStatus = new EmployeeStatus(UUID.randomUUID(), "ACTIVE", "Active employee");
        inactiveStatus = new EmployeeStatus(UUID.randomUUID(), "INACTIVE", "Inactive employee");
    }

    @Test
    void getAllStatuses_ReturnsAllStatuses() {
        when(employeeStatusService.getAllStatuses())
                .thenReturn(Flux.just(activeStatus, inactiveStatus));

        StepVerifier.create(employeeStatusController.getAllStatuses())
                .expectNext(activeStatus)
                .expectNext(inactiveStatus)
                .verifyComplete();
    }

    @Test
    void getAllStatuses_WhenEmpty_ReturnsEmptyFlux() {
        when(employeeStatusService.getAllStatuses()).thenReturn(Flux.empty());

        StepVerifier.create(employeeStatusController.getAllStatuses())
                .verifyComplete();
    }
}
