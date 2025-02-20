package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Person;

/**
 * This a unit test of the PersonRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        personRepository.save(testPerson);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        personRepository.delete(testPerson);
    }

        
    @Test
    /**
     * This method tests that the setup is able to correctly save a person entity to the database and that this can be retrived using findById
     */
    void testSaveAndFindById()
    {
        Person person = personRepository.findById(testPerson.getId()).orElse(null);
        assertNotNull(person);
        assertEquals(testPerson.getName(), person.getName());
    }

    @Test
    /**
     * This method tests the findByName function
     * TODO update this with any new methods for the person repository
     */
    void findByNameTest()
    {
        List<Person> result = personRepository.findByName("test");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPerson.getName(), result.get(0).getName());

        result = personRepository.findByName("notARealName");
        assertNotNull(result);
        assertEquals(0, result.size());
    }
}
