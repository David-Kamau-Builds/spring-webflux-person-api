
package com.start.demo.service;

import com.start.demo.model.Position;
import com.start.demo.repository.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    @Autowired
    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    public Flux<Position> getAllPositions() {
        return positionRepository.findAll();
    }

    public Mono<Position> createPosition(Position position) {
        Position newPosition = new Position(null, position.name(), position.description());
        return positionRepository.save(newPosition);
    }

    public Mono<Void> deletePosition(UUID id) {
        return positionRepository.deleteById(id);
    }
}
