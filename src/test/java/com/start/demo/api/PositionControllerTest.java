package com.start.demo.api;

import com.start.demo.model.Position;
import com.start.demo.service.PositionService;
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

class PositionControllerTest {

    @InjectMocks
    private PositionController positionController;

    @Mock
    private PositionService positionService;

    private Position testPosition;
    private UUID positionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        positionId = UUID.randomUUID();
        testPosition = new Position(positionId, "Software Engineer", "Develops software");
    }

    @Test
    void getAllPositions_ReturnsFluxOfPositions() {
        when(positionService.getAllPositions()).thenReturn(Flux.just(testPosition));

        StepVerifier.create(positionController.getAllPositions())
                .expectNext(testPosition)
                .verifyComplete();
    }

    @Test
    void getAllPositions_WhenEmpty_ReturnsEmptyFlux() {
        when(positionService.getAllPositions()).thenReturn(Flux.empty());

        StepVerifier.create(positionController.getAllPositions())
                .verifyComplete();
    }

    @Test
    void createPosition_ReturnsCreatedPosition() {
        when(positionService.createPosition(any(Position.class))).thenReturn(Mono.just(testPosition));

        StepVerifier.create(positionController.createPosition(testPosition))
                .expectNext(testPosition)
                .verifyComplete();
    }

    @Test
    void deletePosition_ReturnsEmpty() {
        when(positionService.deletePosition(positionId)).thenReturn(Mono.empty());

        StepVerifier.create(positionController.deletePosition(positionId))
                .verifyComplete();
    }
}
