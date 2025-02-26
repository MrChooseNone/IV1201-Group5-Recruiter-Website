package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

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
    private RoleRepository roleRepository;
    private Role testRole;

    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;
    private Person testPerson2;

    @Autowired 
    private AvailabilityRepository availabilityRepository;

    private Availability availability;

    private long systemTime;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        systemTime=System.currentTimeMillis();

        testRole = new Role();
        testRole.setName("test role");
        roleRepository.save(testRole);


        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        personRepository.save(testPerson);

        testPerson2=new Person();
        testPerson2.setName("test2");
        testPerson2.setSurname("tests2son");
        testPerson2.setEmail("test@tes2t.test");
        testPerson2.setPassword("testPassword");
        testPerson2.setPnr("12345668-1234");
        testPerson2.setRole(testRole);
        testPerson2.setUsername("username2");
        personRepository.save(testPerson2);

        availability= new Availability(testPerson, new java.sql.Date(systemTime+44444), new java.sql.Date(systemTime+84444));
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

        availabilityRepository.save(new Availability(testPerson,new java.sql.Date(systemTime+44444),new java.sql.Date(systemTime+44444)));
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

        Boolean result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(new java.sql.Date(systemTime), new java.sql.Date(systemTime+22222), testPerson);
        assertEquals(true, result);

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(new java.sql.Date(systemTime), new java.sql.Date(systemTime+444444444), testPerson);
        assertEquals(false, result);

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(new java.sql.Date(systemTime+2222),new java.sql.Date(systemTime+22222), testPerson);
        assertEquals(true, result);

        availabilityRepository.save(new Availability(testPerson, new java.sql.Date(systemTime+2222), new java.sql.Date(systemTime+444444444)));

        result = availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(new java.sql.Date(systemTime), new java.sql.Date(systemTime), testPerson);
        assertEquals(true, result);
    }

    @Test
    /**
     * This tests the existsByFromDateAndToDateAndPerson method
     */
    void existsByFromDateAndToDateAndPersonTest()
    {
        Boolean result = availabilityRepository.existsByFromDateAndToDateAndPerson(new java.sql.Date(systemTime+10000), new java.sql.Date(systemTime+10000), testPerson2);
        assertEquals(false, result);

        result = availabilityRepository.existsByFromDateAndToDateAndPerson(new java.sql.Date(systemTime+44444), new java.sql.Date(systemTime+84444), testPerson);
        assertEquals(true, result);

        availabilityRepository.save(new Availability(testPerson2, new java.sql.Date(systemTime+10000), new java.sql.Date(systemTime+10000)));
        result = availabilityRepository.existsByFromDateAndToDateAndPerson(new java.sql.Date(systemTime+10000), new java.sql.Date(systemTime+10000), testPerson2);
        assertEquals(true, result);
    }

    @Test
    /**
     * This tests the Availability entities constraints
     */
    void availabilityContraintTest()
    {

        availability= new Availability(null, new java.sql.Date(systemTime+10000), new java.sql.Date(systemTime+10000));
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> availabilityRepository.save(availability));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each availability period must belong to one individual"));
    
        availability= new Availability(testPerson, null, new java.sql.Date(systemTime+10000));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> availabilityRepository.save(availability));
        assertEquals(true,e.getConstraintViolations().toString().contains("From date must be non-null"));

        availability= new Availability(testPerson, new java.sql.Date(systemTime+10000), null);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> availabilityRepository.save(availability));
        assertEquals(true,e.getConstraintViolations().toString().contains("To date must be non-null"));

        availability= new Availability(testPerson, new java.sql.Date(systemTime-10000), new java.sql.Date(systemTime+10000));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> availabilityRepository.save(availability));
        assertEquals(true,e.getConstraintViolations().toString().contains("From date must be some time in the future"));

        availability= new Availability(testPerson, new java.sql.Date(systemTime+10000), new java.sql.Date(systemTime-10000));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> availabilityRepository.save(availability));
        assertEquals(true,e.getConstraintViolations().toString().contains("To date must be some time in the future"));

    }
}
