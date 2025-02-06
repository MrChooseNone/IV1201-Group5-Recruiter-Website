package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.entity.Person;
import com.example.demo.repository.PersonRepository;

@Service

//This is an example of how a service class is structured
//Services are where the actual business logic is handeled, 
public class ServiceExample {
    private final PersonRepository personRepository;


    /**
     * Constructs a new instance of the ServiceExample (Spring boot managed).
     *
     * @param personRepository the repository for accessing person database data
     */
    public ServiceExample(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * Returns a basic message, to show the integration of the service is successfull
     *
     * @return a static string "basic service communication"
     */
    public String basicTest() {
        return "basic service communication";
    }

    public String findIfExistsById(Integer person_id)
    {

        Optional<Person> foundPerson=personRepository.findById(person_id);
        if (!foundPerson.isPresent()) {
            return "Person with person_id "+person_id+" did not exist";
        }
        
        Person person = foundPerson.get();
        return "Person with person_id "+person_id+" did exist. Name: "+person.getName();
    }

    public long findCount() {
        return personRepository.count();
    }
}
