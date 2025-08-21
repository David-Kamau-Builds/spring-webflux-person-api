package com.start.demo.service;

import com.start.demo.model.Person;
import com.start.demo.repository.PersonRepository;
import java.util.UUID;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {
  private final PersonRepository personRepository;
  private final R2dbcEntityTemplate template;

  public PersonService(PersonRepository personRepository, R2dbcEntityTemplate template) {
    this.personRepository = personRepository;
    this.template = template;
  }

  public Mono<Person> addPerson(Person person) {
    Person newPerson = new Person(UUID.randomUUID(), person.name(), person.email());
    return template.insert(newPerson);
  }

  public Flux<Person> getAllPeople() {
    return personRepository.findAll();
  }

  public Mono<Person> getPersonById(UUID id) {
    return personRepository.findById(id);
  }

  public Mono<Person> updatePerson(UUID id, Person person) {
    Person updatedPerson = new Person(id, person.name(), person.email());
    return personRepository.save(updatedPerson);
  }

  public Mono<Boolean> deletePerson(UUID id) {
    return personRepository.deleteById(id).thenReturn(true);
  }
}