package com.start.demo;

import com.start.demo.repository.DepartmentRepository;
import com.start.demo.repository.EmployeeRepository;
import com.start.demo.repository.EmployeeStatusRepository;
import com.start.demo.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {"spring.flyway.enabled=false"})
class DemoApplicationTests {

  @MockBean
  private DepartmentRepository departmentRepository;

  @MockBean
  private EmployeeRepository employeeRepository;

  @MockBean
  private EmployeeStatusRepository employeeStatusRepository;

  @MockBean
  private PositionRepository positionRepository;

  @Test
  void contextLoads() {}
}