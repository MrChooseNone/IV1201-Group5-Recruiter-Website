package com.example.demo.service;

import com.example.demo.repository.PersonRepository;

public class PersonService {
    private final PersonRepository personRepository;
        /**
     * Constructs a new instance of the ServiceExample (Spring boot managed).
     *
     * @param personRepository the repository for accessing person database data
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
