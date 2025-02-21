package com.example.demo.service;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.ApplicationNotFoundException;
import com.example.demo.repository.PersonRepository;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
/**
 * This is the class for tests for the PersonService class
 * Based on examples from the following links: 
 * https://ashok-s-nair.medium.com/java-unit-testing-a-spring-boot-service-with-mockito-2362a32fe217 
 * https://www.java67.com/2023/04/difference-between-mockitomock-mock-and.html 
 * 
 */
@SpringBootTest(properties = { "spring.profiles.active=test" })
public class PersonServiceTest {

    //This inserts a mock of the the password encoder
    @Spy
    private PasswordEncoder passwordEncoder;

    //This is used to create a fake personRepository, to avoid actually interacting with the database, since this is a unit test for a service, not an integration test
    @Spy 
    private PersonRepository personRepository;

    //This ensures above is used in place of a real instance in the constructor
    @InjectMocks
    private PersonService personService;

    //This list is used as the mock repository, accessed using the above repository, avoids real database accesses being made to ensure test results are entirely correct
    static private List<Person> savedPeople=new ArrayList<Person>(); 

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
     * This is a test for the RegisterPerson function
     * It is based on the @Mock example from the article "Difference between Mockito.mock(), @Mock and @MockBean annotation in Spring Boot" published on Java67 by javin paul 
     * Link: https://www.java67.com/2023/04/difference-between-mockitomock-mock-and.html 
     * TODO update this when addPerson is updated with more parameters
     */
    public void RegisterPersonTest()
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

        when(personRepository.existsByPnr(anyString())).thenAnswer(invocation -> {

            String pnrArgument=(String)invocation.getArguments()[0];
            for (Person p : savedPeople) {
                if (p.getPnr()==pnrArgument) {
                    return true;
                }
            }
            return false;
        });

        when(personRepository.existsByEmail(anyString())).thenAnswer(invocation -> {

            String emailArgument=(String)invocation.getArguments()[0];
            for (Person p : savedPeople) {
                if (p.getEmail()==emailArgument) {
                    return true;
                }
            }
            return false;
        });

        when(personRepository.existsByUsername(anyString())).thenAnswer(invocation -> {

            String usernameArgument=(String)invocation.getArguments()[0];
            for (Person p : savedPeople) {
                if (p.getUsername()==usernameArgument) {
                    return true;
                }
            }
            return false;
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
        personService.RegisterPerson(name, surname,"pnr","email","password","username");

        //This is used to verify that personRepository save was called once, with any person as the parameter, which should be done in the PersonService RegisterPerson method
        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));

        //We then call the repository to test if it added the desired person
        List<Person> returnFromRepository=personRepository.findByName(name);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromRepository);
        assertEquals(1, returnFromRepository.size());
        assertEquals(name, returnFromRepository.get(0).getName());
        assertEquals(surname, returnFromRepository.get(0).getSurname());

        //We then perform a test that a non-existent person does not exist in the database
        returnFromRepository=personRepository.findByName("otherNonExistentName");
        
        assertNotNull(returnFromRepository);
        assertEquals(0, returnFromRepository.size());

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByName(anyString());

        //We then test that it throws the correct exceptions if a person already exists with certain parameter values
        var e=assertThrowsExactly(IllegalArgumentException.class, ()->personService.RegisterPerson(name, surname,"pnr","email","password","username"));
        assertEquals("PNR is already in use!", e.getMessage());

        e=assertThrowsExactly(IllegalArgumentException.class, ()->personService.RegisterPerson(name, surname,"pnr2","email","password","username"));
        assertEquals("Email is already registered!", e.getMessage());

        e=assertThrowsExactly(IllegalArgumentException.class, ()->personService.RegisterPerson(name, surname,"pnr2","email2","password","username"));
        assertEquals("Username is already taken!", e.getMessage());

        //We then call the function we wish to test
        personService.RegisterPerson(name, surname,"pnr2","email2","password","username2");

        //We then call the repository to test if it added the desired person
        returnFromRepository=personRepository.findByName(name);
        
        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromRepository);
        assertEquals(2, returnFromRepository.size());

        Mockito.verify(this.personRepository, Mockito.times(2)).save(Mockito.any(Person.class));

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
        List<? extends PersonDTO> returnFromService=personService.FindPeopleByName(name);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromService);
        assertEquals(0, returnFromService.size());

        //This is used to verify that personRepository findByName was called once, with any person as the parameter, which should be done in the PersonService FindPeopleByName method
        Mockito.verify(this.personRepository, Mockito.times(1)).findByName(anyString());

        //We then add a person to the repository manually

        personRepository.save(person);

        //We then call the function we wish to test again
        returnFromService=personService.FindPeopleByName(name);

        assertNotNull(returnFromService);
        assertEquals(1, returnFromService.size());
        assertEquals(true, returnFromService.contains(person));

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByName(anyString());
    }

    @Test
    /**
     * This a test for the FindPersonByEmail method, and is very similar to the above in implementation
     */
    public void FindPersonByEmailTest()
    {
        //We first create an example person, with test parameters
        String name="test";
        String surname="testsson";
        String email="email";
        Person person=new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setEmail(email);

        //This is used to define the behaviour of personRepository, since it does not actually exist, specifically this mocks saving to the database by saving to the above defined list
        //Example used from article: https://www.baeldung.com/java-mock-same-method-other-parameters 
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPeople.add((Person)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findByEmail(anyString())).thenAnswer(invocation -> {
            String emailArgument=(String)invocation.getArguments()[0];
            Optional<PersonDTO> pContainer;
            for (PersonDTO a : savedPeople) {
                if (a.getEmail()==emailArgument) {
                    pContainer=Optional.of(a);
                    return pContainer;
                }
            }
            pContainer=Optional.empty();
            return pContainer;
        });

        //We then call the function we wish to test
        Optional<? extends PersonDTO> returnFromService=personService.FindPersonByEmail(email);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromService);
        assertEquals(true, returnFromService.isEmpty());

        //This is used to verify that personRepository findByName was called once, with any person as the parameter, which should be done in the PersonService FindPeopleByName method
        Mockito.verify(this.personRepository, Mockito.times(1)).findByEmail(anyString());

        //We then add a person to the repository manually

        personRepository.save(person);

        //We then call the function we wish to test again
        returnFromService=personService.FindPersonByEmail(email);

        assertNotNull(returnFromService);
        assertEquals(false, returnFromService.isEmpty());
        assertEquals(person,returnFromService.get());

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByEmail(anyString());
    }

    @Test
    /**
     * This a test for the FindPersonByUsername method, and is very similar to the above in implementation
     */
    public void FindPersonByUsernameTest()
    {
        //We first create an example person, with test parameters
        String name="test";
        String surname="testsson";
        String username="username";
        Person person=new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setUsername(username);

        //This is used to define the behaviour of personRepository, since it does not actually exist, specifically this mocks saving to the database by saving to the above defined list
        //Example used from article: https://www.baeldung.com/java-mock-same-method-other-parameters 
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPeople.add((Person)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findByUsername(anyString())).thenAnswer(invocation -> {
            String usernameArgument=(String)invocation.getArguments()[0];
            Optional<PersonDTO> pContainer;
            for (PersonDTO a : savedPeople) {
                if (a.getUsername()==usernameArgument) {
                    pContainer=Optional.of(a);
                    return pContainer;
                }
            }
            pContainer=Optional.empty();
            return pContainer;
        });

        //We then call the function we wish to test
        Optional<? extends PersonDTO> returnFromService=personService.FindPersonByUsername(username);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromService);
        assertEquals(true, returnFromService.isEmpty());

        //This is used to verify that personRepository findByName was called once, with any person as the parameter, which should be done in the PersonService FindPeopleByName method
        Mockito.verify(this.personRepository, Mockito.times(1)).findByUsername(anyString());

        //We then add a person to the repository manually

        personRepository.save(person);

        //We then call the function we wish to test again
        returnFromService=personService.FindPersonByUsername(username);

        assertNotNull(returnFromService);
        assertEquals(false, returnFromService.isEmpty());
        assertEquals(person,returnFromService.get());

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByUsername(anyString());
    }

    
    @Test
    /**
     * This a test for the FindPersonByPnr method, and is very similar to the above in implementation
     */
    public void FindPersonByPnrTest()
    {
        //We first create an example person, with test parameters
        String name="test";
        String surname="testsson";
        String pnr="pnr";
        Person person=new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setPnr(pnr);

        //This is used to define the behaviour of personRepository, since it does not actually exist, specifically this mocks saving to the database by saving to the above defined list
        //Example used from article: https://www.baeldung.com/java-mock-same-method-other-parameters 
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPeople.add((Person)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findByPnr(anyString())).thenAnswer(invocation -> {
            String pnrArgument=(String)invocation.getArguments()[0];
            Optional<PersonDTO> pContainer;
            for (PersonDTO a : savedPeople) {
                if (a.getPnr()==pnrArgument) {
                    pContainer=Optional.of(a);
                    return pContainer;
                }
            }
            pContainer=Optional.empty();
            return pContainer;
        });

        //We then call the function we wish to test
        Optional<? extends PersonDTO> returnFromService=personService.FindPersonByPnr(pnr);

        //We then perform some checks on the result, to confirm it is correct
        assertNotNull(returnFromService);
        assertEquals(true, returnFromService.isEmpty());

        //This is used to verify that personRepository findByName was called once, with any person as the parameter, which should be done in the PersonService FindPeopleByName method
        Mockito.verify(this.personRepository, Mockito.times(1)).findByPnr(anyString());

        //We then add a person to the repository manually

        personRepository.save(person);

        //We then call the function we wish to test again
        returnFromService=personService.FindPersonByPnr(pnr);

        assertNotNull(returnFromService);
        assertEquals(false, returnFromService.isEmpty());
        assertEquals(person,returnFromService.get());

        Mockito.verify(this.personRepository, Mockito.times(1)).save(Mockito.any(Person.class));
        Mockito.verify(this.personRepository, Mockito.times(2)).findByPnr(anyString());
    }
}
