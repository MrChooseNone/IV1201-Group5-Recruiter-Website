package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

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
    private RoleRepository roleRepository;

    private Role testRole;

    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;

    @BeforeEach
    public void setUp() {

        testRole = new Role();
        testRole.setName("test role");
        roleRepository.save(testRole);

        // Initialize test data before each test method
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
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

    @Test
    /**
     * This method tests the findByEmail function
     */
    void findByEmailTest()
    {
        Optional<Person> result = personRepository.findByEmail("test@test.test");
        assertTrue(result.isPresent());
        assertEquals(testPerson, result.get());

        result = personRepository.findByEmail("notARealValue");
        assertFalse(result.isPresent());
    }

    @Test
    /**
     * This method tests the findByUsername function
     */
    void findByUsernameTest()
    {
        Optional<Person> result = personRepository.findByUsername("username");
        assertTrue(result.isPresent());
        assertEquals(testPerson, result.get());

        result = personRepository.findByUsername("notARealValue");
        assertFalse(result.isPresent());
    }

    @Test
    /**
     * This method tests the findByPnr function
     */
    void findByPnrTest()
    {
        Optional<Person> result = personRepository.findByPnr("12345678-1234");
        assertTrue(result.isPresent());
        assertEquals(testPerson, result.get());

        result = personRepository.findByPnr("notARealValue");
        assertFalse(result.isPresent());
    }

    @Test
    /**
     * This tests the existsByEmail function
     */
    void existsByEmailTest()
    {
        assertTrue(personRepository.existsByEmail("test@test.test"));
        assertFalse(personRepository.existsByEmail("notARealValue"));
    }

    @Test
    /**
     * This tests the existsByUsername function
     */
    void existsByUsernameTest()
    {
        assertTrue(personRepository.existsByUsername("username"));
        assertFalse(personRepository.existsByUsername("notARealValue"));
    }

    @Test
    /**
     * This tests the existsByPnr function
     */
    void existsByPnrTest()
    {
        assertTrue(personRepository.existsByPnr("12345678-1234"));
        assertFalse(personRepository.existsByPnr("notARealValue"));
    }

    @Test
    /**
     * This tests the constrains for the person class, and that these work correctly
     */
    void testPersonConstraints()
    {
        //We test the name constraints
        testPerson=new Person();
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Name must not be null"));

        testPerson=new Person();
        testPerson.setName("");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Name can not be an empty string"));

        //We test the surname constraints
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Surname should not be null"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Surname can not be an empty string"));

        //We then test the pnr constraints
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Pnr may not be null"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Pnr may not be an empty string"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Pnr must follow the following format yyyymmdd-nnnn, with each char replaced with appropriate numbers"));
    
        //We then test the email constraints
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Email must not be null"));
        
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Email may not be an empty string"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("notAnEmail");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Email must be a possible email address, following the format something@something.something, with something being any alphanumeric text"));

        //We test the password constraints

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.com");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Password must not be null"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.com");
        testPerson.setPassword("");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Password can not be an empty string"));

        //We then test the role constraints

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.com");
        testPerson.setPnr("12345678-1234");
        testPerson.setUsername("username");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each person must have a role"));

        //And then we test the username constraints

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Username must not be null"));

        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("");
        
        e = assertThrowsExactly(ConstraintViolationException.class, () -> personRepository.save(testPerson));
        assertEquals(true,e.getConstraintViolations().toString().contains("Username can not be an empty string"));
    }
}
