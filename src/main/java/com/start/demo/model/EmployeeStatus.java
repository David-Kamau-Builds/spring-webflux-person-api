package com.start.demo.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.UUID;

@Table("employee_statuses")
public record EmployeeStatus(
    @Id UUID id,
    String name,
    String description
) {}
