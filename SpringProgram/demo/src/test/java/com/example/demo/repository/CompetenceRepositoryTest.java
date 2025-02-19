package com.example.demo.repository;import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;

/**
 * This a unit test of the CompetenceRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions
public class CompetenceRepositoryTest {
    @Autowired
    private CompetenceRepository competenceRepository;

    private Competence competence;

    
    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        competence = new Competence();
        competence.setName("testCompetence");
        competenceRepository.save(competence);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        competenceRepository.delete(competence);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a competence entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        Competence findResult = competenceRepository.findById(competence.getCompetenceId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(competence.getName(), findResult.getName());
    }

    @Test
    /**
     * This method tests the findByName function
     */
    void findByNameTest() {
        Competence result = competenceRepository.findByName("testCompetence");
        assertNotNull(result);
        assertEquals(competence.getName(), result.getName());

        result = competenceRepository.findByName("notARealName");
        assertNull(result);
    }
}
