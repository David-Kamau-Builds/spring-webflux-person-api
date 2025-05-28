package com.start.demo.api;

import com.start.demo.model.Person;
import com.start.demo.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/persons")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> addPerson(@RequestBody Person person) {
        UUID id = UUID.randomUUID();
        Person personWithId = new Person(id, person.name(), person.email());

        return personService.addPerson(personWithId)
                .map(savedPerson-> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("person", savedPerson);
                    response.put("message", "Person added successfully");
                    return ResponseEntity.status(HttpStatus.CREATED).body(response);
                });
    }


    @GetMapping
    public Mono<ResponseEntity<Map<String, Object>>> getAllPeople() {
        return personService.getAllPeople()
                .collectList()
                .map(people -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("people", people);
                    response.put("message", "People retrieved successfully");
                    return ResponseEntity.ok(response);
                });
    }


    @GetMapping(path = "{id}")
    public Mono<ResponseEntity<Map<String, Object>>> getPersonById(@PathVariable UUID id) {
        return personService.getPersonById(id)
                .map(person -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("person", person);
                    response.put("message", "Retrieval was successful");
                    return ResponseEntity.ok(response);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "Person not found")));
    }


    @DeleteMapping(path = "{id}")
    public Mono<ResponseEntity<Map<String, Object>>> deletePersonById(@PathVariable UUID id) {
        return personService.getPersonById(id)
                .flatMap(person -> personService.deletePerson(id)
                        .map(deleted -> {
                            Map<String, Object> response = new HashMap<>();
                            if (deleted) {
                                response.put("message", "Person with id " + id + " deleted");
                                return ResponseEntity.ok(response);
                            } else {
                                response.put("message", "Person with id " + id + " not found");
                                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
                            }
                        }))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createNotFoundResponse(id)));
                }

    private Map<String, Object> createNotFoundResponse(UUID id) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Person with id " + id + " not found");
        return response;

    }
    @PutMapping(path = "{id}")
    public Mono<ResponseEntity<Map<String, Object>>> updatePerson(@PathVariable UUID id, @RequestBody Person person) {
        return personService.getPersonById(id)
                .flatMap(existingPerson -> {
                    Person updatedPerson = new Person(id, person.name(), person.email());
                    return personService.updatePerson(id, updatedPerson)
                            .map(p -> {
                                Map<String, Object> response = new HashMap<>();
                                response.put("person", p);
                                response.put("message", "Person with id " + id + " updated");
                                return ResponseEntity.ok(response);
                            });

                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createNotFoundResponse(id)));
    }
}