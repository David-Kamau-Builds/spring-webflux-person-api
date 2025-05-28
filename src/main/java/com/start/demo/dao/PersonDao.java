package com.start.demo.dao;

import com.start.demo.model.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PersonDao {

    Mono<Person> insertPerson(Person person);

    Mono<Person> selectPersonById(UUID id);

    Flux<Person> selectAllPeople();

    Mono<Boolean> deletePersonById(UUID id);

    Mono<Person> updatePersonById(UUID id, Person person);
}
