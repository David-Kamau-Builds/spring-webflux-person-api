package com.start.demo.repository;

import com.start.demo.model.Person;
import java.util.UUID;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<Person, UUID> {}
