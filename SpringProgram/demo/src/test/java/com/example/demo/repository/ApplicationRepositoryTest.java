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

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Person;

/**
 * This a unit test of the Application class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions

public class ApplicationRepositoryTest {
    @Autowired 
    private ApplicationRepository applicationRepository;

    private Application application;
    private Application application2;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method

        application= new Application(null, null, null);
        application2= new Application(null, null, null);
        applicationRepository.save(application);
        applicationRepository.save(application2);

    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        applicationRepository.delete(application);
        applicationRepository.delete(application2);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a application entity
     * to the database and that this can be retrived using findById
     */
    void testSaveAndFindById() {
        Application findResult = applicationRepository.findById(application.getApplicationId()).orElse(null);
        assertNotNull(findResult);
        assertEquals(application.getApplicationDate(), findResult.getApplicationDate());
    }

    @Test
    /**
     * This is a test for the findAllByApplicationStatus method
     */
    void findAllByApplicationStatusTest()
    {
        List<Application> findResult = applicationRepository.findAllByApplicationStatus(ApplicationStatus.unchecked);
        assertNotNull(findResult);
        assertEquals(2, findResult.size());
        assertEquals(application, findResult.get(0));
        assertEquals(application2, findResult.get(1));


        findResult = applicationRepository.findAllByApplicationStatus(ApplicationStatus.accepted);
        assertNotNull(findResult);
        assertEquals(0, findResult.size());

        applicationRepository.delete(application2);

        findResult = applicationRepository.findAllByApplicationStatus(ApplicationStatus.unchecked);
        assertNotNull(findResult);
        assertEquals(1, findResult.size());
        assertEquals(application, findResult.get(0));

    }
}
