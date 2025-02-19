package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;

/**
 * This a unit test of the CompetenceTranslationRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions

public class CompetenceTranslationRepositoryTest {
    @Autowired
    private LanguageRepository languageRepository;

    private Language language;

    @Autowired
    private CompetenceTranslationRepository competenceTranslationRepository;

    private CompetenceTranslation competenceTranslation;


    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        language = new Language();
        language.SetLanguageName("testLanguage");
        languageRepository.save(language);

        competenceTranslation = new CompetenceTranslation();
        competenceTranslation.SetLanguage(language);
        competenceTranslationRepository.save(competenceTranslation);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        competenceTranslationRepository.delete(competenceTranslation);
        languageRepository.delete(language);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a competence translation entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        CompetenceTranslation findResult = competenceTranslationRepository.findById(competenceTranslation.getCompetenceTranslationId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(competenceTranslation.getLanguage(), findResult.getLanguage());
    }

    @Test
    /**
     * This method tests the findByLanguage_id function
     */
    void findByNameTest() {
        List<CompetenceTranslation> result = competenceTranslationRepository.findByLanguage_id(language.getLanguageId());
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(competenceTranslation, result.get(0));

        result = competenceTranslationRepository.findByLanguage_id(language.getLanguageId()+1);
        assertEquals(0, result.size());
    }
}
