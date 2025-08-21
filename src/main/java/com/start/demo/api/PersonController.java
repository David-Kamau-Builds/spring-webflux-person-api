package com.start.demo.api;

import com.start.demo.model.Person;
import com.start.demo.service.PersonService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

  private final PersonService personService;

  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Person> addPerson(@Valid @RequestBody Person person) {
    return personService.addPerson(person);
  }

  @GetMapping
  public Flux<Person> getAllPeople() {
    return personService.getAllPeople();
  }

  @GetMapping("/{id}")
  public Mono<Person> getPersonById(@PathVariable UUID id) {
    return personService.getPersonById(id);
  }

  @PutMapping("/{id}")
  public Mono<Person> updatePerson(@PathVariable UUID id, @Valid @RequestBody Person person) {
    return personService.updatePerson(id, person);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public Mono<Void> deletePerson(@PathVariable UUID id) {
    return personService.deletePerson(id).then();
  }
}