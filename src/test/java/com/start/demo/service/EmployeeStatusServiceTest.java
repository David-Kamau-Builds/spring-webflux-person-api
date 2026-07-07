package com.start.demo.service;

import com.start.demo.model.EmployeeStatus;
import com.start.demo.repository.EmployeeStatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

class EmployeeStatusServiceTest {

    @InjectMocks
    private EmployeeStatusService employeeStatusService;

    @Mock
    private EmployeeStatusRepository employeeStatusRepository;

    private EmployeeStatus activeStatus;
    private EmployeeStatus inactiveStatus;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        activeStatus = new EmployeeStatus(UUID.randomUUID(), "ACTIVE", "Active employee");
        inactiveStatus = new EmployeeStatus(UUID.randomUUID(), "INACTIVE", "Inactive employee");
    }

    @Test
    void getAllStatuses_ReturnsAllStatusesFromRepository() {
        when(employeeStatusRepository.findAll())
                .thenReturn(Flux.just(activeStatus, inactiveStatus));

        StepVerifier.create(employeeStatusService.getAllStatuses())
                .expectNext(activeStatus)
                .expectNext(inactiveStatus)
                .verifyComplete();
    }

    @Test
    void getAllStatuses_WhenRepositoryEmpty_ReturnsEmptyFlux() {
        when(employeeStatusRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(employeeStatusService.getAllStatuses())
                .verifyComplete();
    }
}
