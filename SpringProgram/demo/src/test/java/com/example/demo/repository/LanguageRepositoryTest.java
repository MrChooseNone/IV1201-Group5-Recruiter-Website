package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Language;

import jakarta.validation.ConstraintViolationException;

/**
 * This a unit test of the LanguageRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb", // This specifies the in-memory database url
        "spring.jpa.hibernate.ddl-auto=create-drop" // This is used to specify that the database schema should be
                                                    // dropped after the test is over Link:
                                                    // https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring
}) // This is done to only load the neccesary context for testing the repository +
   // creating a in-memory database for testing purposes + ensuring test are run in
   // transactions

public class LanguageRepositoryTest {
    @Autowired
    private LanguageRepository languageRepository;

    private Language languge;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        languge = new Language();
        languge.SetLanguageName("testLanguage");
        languageRepository.save(languge);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        languageRepository.delete(languge);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a language entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        Language findResult = languageRepository.findById(languge.getLanguageId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(languge.getLanguageName(), findResult.getLanguageName());
    }

    @Test
    /**
     * This method tests the findByName function
     */
    void findByNameTest() {
        Language result = languageRepository.findByName("testLanguage");
        assertNotNull(result);
        assertEquals(languge.getLanguageName(), result.getLanguageName());

        result = languageRepository.findByName("notARealName");
        assertNull(result);
    }

    @Test
    /**
     * This tests the constrains of the language entity
     */
    void languageConstraintTest()
    {
        languge = new Language();
        languge.SetLanguageName(null);
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> languageRepository.saveAndFlush(languge));

        assertEquals(true,e.getConstraintViolations().toString()
        .contains("ConstraintViolationImpl{interpolatedMessage='Language name can not be null', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Language, messageTemplate='Language name can not be null'}")
        &&
        e.getConstraintViolations().toString()
        .contains("ConstraintViolationImpl{interpolatedMessage='Language name can not be blank', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Language, messageTemplate='Language name can not be blank'}")
        );

        languge = new Language();
        languge.SetLanguageName("");
        e = assertThrowsExactly(ConstraintViolationException.class, () -> languageRepository.saveAndFlush(languge));
        assertEquals("[ConstraintViolationImpl{interpolatedMessage='Language name can not be blank', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Language, messageTemplate='Language name can not be blank'}]",e.getConstraintViolations().toString());
    }
}
