package com.example.demo.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PersonService;

@ExtendWith(MockitoExtension.class)
/**
 * This is the class for tests for the PersonService class
 * Based on examples from the following links: 
 * https://ashok-s-nair.medium.com/java-unit-testing-a-spring-boot-service-with-mockito-2362a32fe217 
 * https://www.java67.com/2023/04/difference-between-mockitomock-mock-and.html 
 * 
 */
public class PersonServiceTest {

    //This is used to create a fake personRepository, to avoid actually interacting with the database, since this is a unit test for a service, not an integration test
    @Mock 
    private PersonRepository personRepository;

    //This list is used as the mock repository, accessed using the above repository, avoids real database accesses being made to ensure test results are entirely correct
    static private List<Person> savedPeople=new ArrayList<Person>(); 

    //This ensures above is used in place of a real instance in the constructor
    @InjectMocks
    private PersonService personService;

    //This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(PersonServiceTest.class);
    }
    
    //This ensures the mock "databases" are clean after each attempt
    @AfterEach
    public void afterEach()
    {
        savedPeople.clear();
    }

    @Test
    /**
     * This is a test for the add person function
     * It is based on the @Mock example from the article "Difference between Mockito.mock(), @Mock and @MockBean annotation in Spring Boot" published on Java67 by javin paul 
     * Link: https://www.java67.com/2023/04/difference-between-mockitomock-mock-and.html 
     * TODO update this when addPerson is updated with more parameters
     */
    public void addPersonTest()
    {
        //We first create an example person, with test parameters
        String name="test";
        String surname="testsson";

        List<Person> savedPeople=new ArrayList<Person>();

        //This is used to define the behaviour of personRepository, since it does not actually exist, specifically this mocks saving to the database by saving to the above defined list
        //Example used from article: https://www.baeldung.com/java-mock-same-method-other-parameters 
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPeople.add((Person)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findByName(anyString())).thenAnswer(invocation -> {

            String nameArgument=(String)invocation.getArguments()[0];
            List<Person> matchingPeople=new ArrayList<Person>();

            for (Person p : savedPeople) {
                if (p.getName()==nameArgument) {
                    matchingPeople.add(p);
                }
            }
            return matchingPeople;
        });

        //We then call the function we wish to test
        personService.AddPerson(name, surname);

        //We then call the repository to test if it added the desired person
        List<Person> returnFromRepository=personRepository.findByName(name);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromRepository);
        assertEquals(1, returnFromRepository.size());
        assertEquals(name, returnFromRepository.get(0).getName());
        assertEquals(surname, returnFromRepository.get(0).getSurname());

        //This is used to verify that personRepository save was called once, with any person as the parameter, which should be done in the PersonService AddPerson method
        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));

        //We then perform a test that a non-existent person does not exist in the database
        returnFromRepository=personRepository.findByName("otherNonExistentName");
        
        assertNotNull(returnFromRepository);
        assertEquals(0, returnFromRepository.size());

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByName(anyString());
    }

    @Test
    /**
     * This a test for the findByName method, and is very similar to the above in implementation
     * TODO update this if the person findBy is replaced
     */
    public void findByNameTest()
    {
        //We first create an example person, with test parameters
        String name="test";
        String surname="testsson";
        Person person=new Person();
        person.setName(name);
        person.setSurname(surname);

        //This is used to define the behaviour of personRepository, since it does not actually exist, specifically this mocks saving to the database by saving to the above defined list
        //Example used from article: https://www.baeldung.com/java-mock-same-method-other-parameters 
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPeople.add((Person)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findByName(anyString())).thenAnswer(invocation -> {

            String nameArgument=(String)invocation.getArguments()[0];
            List<Person> matchingPeople=new ArrayList<Person>();

            for (Person p : savedPeople) {
                if (p.getName()==nameArgument) {
                    matchingPeople.add(p);
                }
            }
            return matchingPeople;
        });

        //We then call the function we wish to test
        List<PersonDTO> returnFromService=(List<PersonDTO>) personService.FindPeopleByName(name);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromService);
        assertEquals(0, returnFromService.size());

        //This is used to verify that personRepository findByName was called once, with any person as the parameter, which should be done in the PersonService FindPeopleByName method
        Mockito.verify(this.personRepository, Mockito.times(1)).findByName(anyString());

        //We then add a person to the repository manually

        personRepository.save(person);

        //We then call the function we wish to test again
        returnFromService=(List<PersonDTO>) personService.FindPeopleByName(name);

        assertNotNull(returnFromService);
        assertEquals(1, returnFromService.size());
        assertEquals(true, returnFromService.contains(person));

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByName(anyString());
    }
}
