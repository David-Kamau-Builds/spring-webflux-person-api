package com.start.demo.service;

import com.start.demo.dao.PersonDao;
import com.start.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PersonService {
    private final PersonDao personDao;

    @Autowired
    public PersonService(@Qualifier("fakeDao") PersonDao personDao) {
        this.personDao = personDao;
    }

    public Mono<Person> addPerson(Person person) {
        return personDao.insertPerson(person);
    }

    public Flux<Person> getAllPeople() {
        return personDao.selectAllPeople();
    }

    public Mono<Person> getPersonById(UUID id) {
        return personDao.selectPersonById(id);
    }

    public Mono<Boolean> deletePerson(UUID id) {
        return personDao.deletePersonById(id);
    }

    public Mono<Person> updatePerson(UUID id, Person person) {
        return personDao.updatePersonById(id, person);
    }

}