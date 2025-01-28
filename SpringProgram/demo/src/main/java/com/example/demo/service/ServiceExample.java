package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.dto.dtoExample;
import com.example.demo.domain.entity.entityExample;
import com.example.demo.repository.RepositoryExample;

@Service

//This is an example of how a service class is structured
//Services are where the actual business logic is handeled, 
public class ServiceExample {
    private final RepositoryExample exampleRepository;


    /**
     * Constructs a new instance of the ServiceExample (Spring boot managed).
     *
     * @param exampleRepository the repository for accessing example database data
     */
    public ServiceExample(RepositoryExample exampleRepository) {
        this.exampleRepository = exampleRepository;
    }

    /**
     * Returns a basic message, to show the integration of the service is successfull
     *
     * @return a static string "basic service communication"
     */
    public String basicTest() {
        return "basic service communication";
    }

    public String findIfExistsById(Integer id)
    {

        Optional<entityExample> foundExample=exampleRepository.findById(id);
        if (!foundExample.isPresent()) {
            return "Entry with id "+id+" did not exist";
        }
        return "Entry with id "+id+" did exist";
    }

    //Return how many entities exist in the example database
    public long findCount()
    {
        return exampleRepository.count();
    }
}
