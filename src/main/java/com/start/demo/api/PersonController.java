package com.start.demo.api;

import com.start.demo.model.Person;
import com.start.demo.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/persons")
@Tag(name = "Person API", description = "CRUD operations for Person entities")
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  @Operation(summary = "Create a new person")
  public Mono<ResponseEntity<Map<String, Object>>> addPerson(@Valid @RequestBody Person person) {
    return personService
        .addPerson(person)
        .map(
            savedPerson -> {
              return ResponseEntity.status(HttpStatus.CREATED)
                  .body(createSuccessResponse("person", savedPerson, "Person added successfully"));
            });
  }

  @GetMapping
  @Operation(summary = "Get all people")
  public Flux<Person> getAllPeople() {
    return personService.getAllPeople();
  }

  @GetMapping(path = "{id}")
  @Operation(summary = "Get person by ID")
  public Mono<ResponseEntity<Map<String, Object>>> getPersonById(@PathVariable UUID id) {
    return personService
        .getPersonById(id)
        .map(
            person ->
                ResponseEntity.ok(
                    createSuccessResponse("person", person, "Retrieval was successful")))
        .defaultIfEmpty(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("Person not found")));
  }

  @DeleteMapping(path = "{id}")
  @Operation(summary = "Delete person by ID")
  public Mono<ResponseEntity<Map<String, Object>>> deletePersonById(@PathVariable UUID id) {
    return personService
        .deletePerson(id)
        .map(
            deleted -> {
              if (deleted) {
                return ResponseEntity.ok(
                    createSuccessResponse(null, null, "Person with id " + id + " deleted"));
              } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("Person with id " + id + " not found"));
              }
            });
  }

  @PutMapping(path = "{id}")
  @Operation(summary = "Update person by ID")
  public Mono<ResponseEntity<Map<String, Object>>> updatePerson(
      @PathVariable UUID id, @Valid @RequestBody Person person) {
    Person updatedPerson = new Person(id, person.name(), person.email());
    return personService
        .updatePerson(id, updatedPerson)
        .map(
            p ->
                ResponseEntity.ok(
                    createSuccessResponse("person", p, "Person with id " + id + " updated")))
        .defaultIfEmpty(
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse("Person with id " + id + " not found")));
  }

  private Map<String, Object> createSuccessResponse(String key, Object data, String message) {
    if (key != null && data != null) {
      return Map.of(key, data, "message", message);
    }
    return Map.of("message", message);
  }

  private Map<String, Object> createErrorResponse(String message) {
    return Map.of("message", message);
  }
}
