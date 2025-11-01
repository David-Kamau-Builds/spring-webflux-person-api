package com.start.demo.repository;

import com.start.demo.model.Position;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PositionRepository extends ReactiveCrudRepository<Position, UUID> {}
