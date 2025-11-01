
package com.start.demo.api;

import com.start.demo.model.Position;
import com.start.demo.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.UUID;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api/v1/positions")
@Tag(name = "Positions", description = "APIs for retrieving positions")
public class PositionController {

    private final PositionService positionService;

    @Autowired
    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    @Operation(summary = "Get all positions", description = "Retrieves a list of all available positions")
    public Flux<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new position", description = "Creates a new position")
    public Mono<Position> createPosition(@Valid @RequestBody Position position) {
        return positionService.createPosition(position);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete position", description = "Deletes a position by its ID")
    public Mono<Void> deletePosition(@PathVariable UUID id) {
        return positionService.deletePosition(id);
    }
}
