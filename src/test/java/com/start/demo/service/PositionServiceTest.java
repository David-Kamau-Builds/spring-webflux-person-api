package com.start.demo.service;

import com.start.demo.model.Position;
import com.start.demo.repository.PositionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PositionServiceTest {

    @InjectMocks
    private PositionService positionService;

    @Mock
    private PositionRepository positionRepository;

    private Position testPosition;
    private UUID positionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        positionId = UUID.randomUUID();
        testPosition = new Position(positionId, "Software Engineer", "Develops software");
    }

    @Test
    void getAllPositions_ReturnsAllPositions() {
        when(positionRepository.findAll()).thenReturn(Flux.just(testPosition));

        StepVerifier.create(positionService.getAllPositions())
                .expectNext(testPosition)
                .verifyComplete();
    }

    @Test
    void getAllPositions_WhenEmpty_ReturnsEmptyFlux() {
        when(positionRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(positionService.getAllPositions())
                .verifyComplete();
    }

    @Test
    void createPosition_SavesAndReturnsPosition() {
        Position input = new Position(null, "Software Engineer", "Develops software");
        when(positionRepository.save(any(Position.class))).thenReturn(Mono.just(testPosition));

        StepVerifier.create(positionService.createPosition(input))
                .expectNext(testPosition)
                .verifyComplete();
    }

    @Test
    void deletePosition_CompletesSuccessfully() {
        when(positionRepository.deleteById(positionId)).thenReturn(Mono.empty());

        StepVerifier.create(positionService.deletePosition(positionId))
                .verifyComplete();
    }
}
