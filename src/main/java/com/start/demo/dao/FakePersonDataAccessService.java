package com.start.demo.dao;

import com.start.demo.model.Person;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Repository("fakeDao")
public class FakePersonDataAccessService implements PersonDao {
    private final Map<UUID, Person> DB = new HashMap<>();

    @Override
    public Mono<Person> insertPerson(Person person) {
        DB.put(person.id(), person);
        return Mono.just(person);
    }

    @Override
    public Mono<Person> selectPersonById(UUID id) {
        return Mono.justOrEmpty(DB.get(id));
    }

    @Override
    public Flux<Person> selectAllPeople() {
        return Flux.fromIterable(DB.values());
    }

    @Override
    public Mono<Boolean> deletePersonById(UUID id) {
        if (DB.containsKey(id)) {
            DB.remove(id);
            return Mono.just(true);
        }
        return Mono.just(false);
    }

    @Override
    public Mono<Person> updatePersonById(UUID id, Person person) {
        if (DB.containsKey(id)) {
            Person updatedPerson = new Person(id, person.name(), person.email());
            DB.put(id, updatedPerson);
            return Mono.just(updatedPerson);
        }
        return Mono.empty();
    }

}