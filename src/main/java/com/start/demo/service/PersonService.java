package com.start.demo.service;

import com.start.demo.model.Person;
import com.start.demo.repository.PersonRepository;
import java.util.UUID;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class PersonService {
  private final PersonRepository personRepository;

  public PersonService(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  public Mono<Person> addPerson(Person person) {
    return personRepository.save(person);
  }

  public Flux<Person> getAllPeople() {
    return personRepository.findAll();
  }

  public Mono<Person> getPersonById(UUID id) {
    return personRepository.findById(id);
  }

  public Mono<Boolean> deletePerson(UUID id) {
    return personRepository
        .existsById(id)
        .flatMap(
            exists -> {
              if (exists) {
                return personRepository.deleteById(id).then(Mono.just(true));
              }
              return Mono.just(false);
            });
  }

  public Mono<Person> updatePerson(UUID id, Person person) {
    return personRepository
        .existsById(id)
        .flatMap(exists -> exists ? personRepository.save(person) : Mono.empty());
  }
}
