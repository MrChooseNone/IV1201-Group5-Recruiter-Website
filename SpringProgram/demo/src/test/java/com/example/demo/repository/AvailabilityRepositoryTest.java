package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Person;

/**
 * This a unit test of the AvailabilityRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions
public class AvailabilityRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;
    private Person testPerson2;

    @Autowired 
    private AvailabilityRepository availabilityRepository;

    private Availability availability;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        testPerson=new Person();
        personRepository.save(testPerson);
        testPerson2=new Person();
        personRepository.save(testPerson2);

        availability= new Availability(testPerson, Date.valueOf("2000-01-01"), Date.valueOf("2000-02-01"));
        availabilityRepository.save(availability);

    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        personRepository.delete(testPerson);
        personRepository.delete(testPerson2);
        availabilityRepository.delete(availability);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a availability entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        Availability findResult = availabilityRepository.findById(availability.getAvailabilityId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(testPerson, findResult.getPerson());
    }

    @Test
    /**
     * This tests the method findAllByPerson
     */
    void findAllByPersonTest()
    {
        List<Availability> findResult = availabilityRepository.findAllByPerson(testPerson);
        assertNotNull(findResult);
        assertEquals(1, findResult.size());
        assertEquals(testPerson, findResult.get(0).getPerson());

        findResult = availabilityRepository.findAllByPerson(testPerson2);
        assertNotNull(findResult);
        assertEquals(0, findResult.size());

        availabilityRepository.save(new Availability(testPerson,null,null));
        findResult = availabilityRepository.findAllByPerson(testPerson);
        assertNotNull(findResult);
        assertEquals(2, findResult.size());
        assertEquals(testPerson, findResult.get(0).getPerson());
        assertEquals(testPerson, findResult.get(1).getPerson());

    }

    @Test
    /**
     * This tests the existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson method
     */
    void existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPersonTest()
    {
        Boolean result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-01-02"), testPerson);
        assertEquals(true, result);

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-03-01"), testPerson);
        assertEquals(false, result);

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(Date.valueOf("2000-01-03"), Date.valueOf("2000-01-04"), testPerson);
        assertEquals(true, result);

        availabilityRepository.save(new Availability(testPerson, Date.valueOf("2000-01-01"), Date.valueOf("2000-05-02")));

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-03-01"), testPerson);
        assertEquals(true, result);
    }

    @Test
    /**
     * This tests the existsByFromDateAndToDateAndPerson method
     */
    void existsByFromDateAndToDateAndPersonTest()
    {
        Boolean result = availabilityRepository.existsByFromDateAndToDateAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-01-02"), testPerson);
        assertEquals(false, result);

        result = availabilityRepository.existsByFromDateAndToDateAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-02-01"), testPerson);
        assertEquals(true, result);

        availabilityRepository.save(new Availability(testPerson, Date.valueOf("2000-01-01"), Date.valueOf("2000-01-02")));

        result = availabilityRepository.existsByFromDateAndToDateAndPerson(Date.valueOf("2000-01-01"), Date.valueOf("2000-01-02"), testPerson);
        assertEquals(true, result);
    }
}
