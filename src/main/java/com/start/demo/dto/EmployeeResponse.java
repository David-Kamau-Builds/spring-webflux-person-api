package com.start.demo.dto;

import com.start.demo.model.EmployeeStatus;
import com.start.demo.model.Position;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EmployeeResponse(
    UUID id,
    String firstName,
    String lastName,
    String fullName,
    String email,
    String phone,
    String departmentName,
    String positionName,
    BigDecimal salary,
    LocalDate hireDate,
    String statusName
) {}