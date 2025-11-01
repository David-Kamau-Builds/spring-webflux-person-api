package com.start.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

@Table("positions")
public record Position(
    @Id UUID id,
    String name,
    String description
) {}
