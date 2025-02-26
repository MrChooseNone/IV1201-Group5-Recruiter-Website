package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

/**
 * This a unit test of the CompetenceProfileRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions

public class CompetenceProfileRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    private Role testRole;

    @Autowired
    private CompetenceProfileRepository competenceProfileRepository;

    private CompetenceProfile competenceProfile;

    @Autowired
    private PersonRepository personRepository;

    private Person testPerson;
    private Person testPerson2;

    @Autowired
    private CompetenceRepository competenceRepository;
    private Competence competence;
    private Competence competence2;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        testRole = new Role();
        testRole.setName("test role");
        roleRepository.save(testRole);


        competence = new Competence();
        competence.setName("testCompetence");
        competence2 = new Competence();
        competence2.setName("testCompetence2");
        competenceRepository.save(competence);
        competenceRepository.save(competence2);

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

        competenceProfile = new CompetenceProfile(testPerson, competence, 0);
        competenceProfileRepository.save(competenceProfile);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        competenceProfileRepository.delete(competenceProfile);
        personRepository.delete(testPerson);
        personRepository.delete(testPerson2);
        competenceRepository.delete(competence);
        competenceRepository.delete(competence2);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a competence entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        CompetenceProfile findResult = competenceProfileRepository.findById(competenceProfile.getCompetenceProfileId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(competence, findResult.getCompetenceDTO());
        assertEquals(testPerson, findResult.getPerson());
    }

    @Test
    /**
     * This method tests findAllByPerson
     */
    void findAllByPersonTest()
    {
        List<CompetenceProfile> results = competenceProfileRepository.findAllByPerson(testPerson);
        assertEquals(1, results.size());
        assertEquals(competence, results.get(0).getCompetenceDTO());
        assertEquals(testPerson, results.get(0).getPerson());

        results = competenceProfileRepository.findAllByPerson(testPerson2);
        assertEquals(0, results.size());
    }

    @Test
    /**
     * This method tests existsByPersonAndCompetenceAndYearsOfExperience
     */
    void existsByPersonAndCompetenceAndYearsOfExperienceTest()
    {
        Boolean result=competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(testPerson, competence, 1.0);
        assertEquals(false, result);
        result=competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(testPerson2, competence, 0.0);
        assertEquals(false, result);
        result=competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(testPerson, competence2, 0.0);
        assertEquals(false, result);
        result=competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(testPerson, competence, 0.0);
        assertEquals(true, result);
    }

    @Test
    /**
     * This tests the competence profile constraints
     */
    void competenceProfileConstraintTest()
    {
        competenceProfile = new CompetenceProfile(null, competence, 0);
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> competenceProfileRepository.save(competenceProfile));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each profile must belong to a person"));

        competenceProfile = new CompetenceProfile(testPerson, null, 0);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> competenceProfileRepository.save(competenceProfile));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each profile must correspond to a competence"));

        competenceProfile = new CompetenceProfile(testPerson, competence, -1.0);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> competenceProfileRepository.save(competenceProfile));
        assertEquals(true,e.getConstraintViolations().toString().contains("Years of experience must be non-negative"));

    }

}
