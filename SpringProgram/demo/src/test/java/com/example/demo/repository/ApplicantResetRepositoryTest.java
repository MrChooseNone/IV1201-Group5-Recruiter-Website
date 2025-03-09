package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.ApplicantReset;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

/**
 * This a unit test of the ApplicantReset repository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions
public class ApplicantResetRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    private Role testRole;

    @Autowired
    private PersonRepository personRepository;
    private Person testPerson;

    @Autowired
    private ApplicantResetRepository applicantResetRepository;

    private ApplicantReset reset;

    @BeforeEach
    public void setUp() {
        
        testRole = new Role();
        testRole.setName("test role");
        testPerson=new Person();
        testPerson.setName("test");
        testPerson.setSurname("testsson");
        testPerson.setEmail("test@test.test");
        testPerson.setPassword("testPassword");
        testPerson.setPnr("12345678-1234");
        testPerson.setRole(testRole);
        testPerson.setUsername("username");

        roleRepository.save(testRole);
        personRepository.save(testPerson);

        reset = new ApplicantReset();
        reset.setPerson(testPerson);
        reset.setRandomLong(Long.valueOf(0));
        reset.setResetDate("Date");
        applicantResetRepository.save(reset);
    }

    @AfterEach
    public void tearDown()
    {
        applicantResetRepository.delete(reset);
        roleRepository.delete(testRole);
        personRepository.delete(testPerson);

    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a application entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        ApplicantReset findResult = applicantResetRepository.findById(reset.getResetId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(reset.getResetId(),findResult.getResetId());
    }

    @Test
    /**
     * This tests the findByPersonAndResetDateAndRandomLong method
     */
    void findByPersonAndResetDateAndRandomLongTest()
    {
        Optional<ApplicantReset> result = applicantResetRepository.findByPersonAndResetDateAndRandomLong(reset.getPerson(), reset.getResetDate(), reset.getRandomLong());
        assertEquals(reset, result.get());

        result = applicantResetRepository.findByPersonAndResetDateAndRandomLong(null, reset.getResetDate(), reset.getRandomLong());
        assertFalse(result.isPresent());

        result = applicantResetRepository.findByPersonAndResetDateAndRandomLong(reset.getPerson(), "", reset.getRandomLong());
        assertFalse(result.isPresent());

        result = applicantResetRepository.findByPersonAndResetDateAndRandomLong(reset.getPerson(), reset.getResetDate(), Long.valueOf(32));
        assertFalse(result.isPresent());
    }

    @Test
    /**
     * This tests the ApplicantReset integation level constraints
     */
    void ApplicantResetConstraint()
    {
        reset = new ApplicantReset(null, "test", Long.valueOf(0));
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> applicantResetRepository.save(reset));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each applicant account reset must be for a specific person"));

        reset = new ApplicantReset(testPerson, null, Long.valueOf(0));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicantResetRepository.save(reset));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each reset must have a specific expiration date"));

        reset = new ApplicantReset(testPerson, "", Long.valueOf(0));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicantResetRepository.save(reset));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each expiration date much not be empty"));

        reset = new ApplicantReset(testPerson, "", null);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicantResetRepository.save(reset));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each reset must have a specific random number"));

    }
}
