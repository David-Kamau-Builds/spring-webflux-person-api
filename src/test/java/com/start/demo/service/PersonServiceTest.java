package com.start.demo.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.start.demo.model.Person;
import com.start.demo.repository.PersonRepository;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

  @Mock private PersonRepository personRepository;

  private PersonService personService;

  @BeforeEach
  void setUp() {
    personService = new PersonService(personRepository);
  }

  @Test
  void shouldAddPerson() {
    Person person = new Person(UUID.randomUUID(), "John Doe", "john@example.com");
    when(personRepository.save(any(Person.class))).thenReturn(Mono.just(person));

    StepVerifier.create(personService.addPerson(person)).expectNext(person).verifyComplete();
  }

  @Test
  void shouldGetAllPeople() {
    Person person1 = new Person(UUID.randomUUID(), "John Doe", "john@example.com");
    Person person2 = new Person(UUID.randomUUID(), "Jane Doe", "jane@example.com");
    when(personRepository.findAll()).thenReturn(Flux.just(person1, person2));

    StepVerifier.create(personService.getAllPeople())
        .expectNext(person1)
        .expectNext(person2)
        .verifyComplete();
  }

  @Test
  void shouldGetPersonById() {
    UUID id = UUID.randomUUID();
    Person person = new Person(id, "John Doe", "john@example.com");
    when(personRepository.findById(id)).thenReturn(Mono.just(person));

    StepVerifier.create(personService.getPersonById(id)).expectNext(person).verifyComplete();
  }
}
