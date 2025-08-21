package com.start.demo;

import com.start.demo.model.Person;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DemoApplicationTests {

  @Autowired private WebTestClient webTestClient;

  @Test
  void contextLoads() {}

  @Test
  void shouldCreatePerson() {
    Person person = new Person(null, "John Doe", "john@example.com");

    webTestClient
        .post()
        .uri("/api/v1/persons")
        .bodyValue(person)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectBody()
        .jsonPath("$.person.name")
        .isEqualTo("John Doe")
        .jsonPath("$.person.email")
        .isEqualTo("john@example.com")
        .jsonPath("$.message")
        .isEqualTo("Person added successfully");
  }

  @Test
  void shouldGetAllPersons() {
    webTestClient.get().uri("/api/v1/persons").exchange().expectStatus().isOk();
  }

  @Test
  void shouldReturnNotFoundForNonExistentPerson() {
    UUID nonExistentId = UUID.randomUUID();

    webTestClient
        .get()
        .uri("/api/v1/persons/{id}", nonExistentId)
        .exchange()
        .expectStatus()
        .isNotFound()
        .expectBody()
        .jsonPath("$.message")
        .isEqualTo("Person not found");
  }

  @Test
  void shouldValidatePersonInput() {
    Person invalidPerson = new Person(null, "", "invalid-email");

    webTestClient
        .post()
        .uri("/api/v1/persons")
        .bodyValue(invalidPerson)
        .exchange()
        .expectStatus()
        .isBadRequest()
        .expectBody()
        .jsonPath("$.message")
        .exists();
  }
}
