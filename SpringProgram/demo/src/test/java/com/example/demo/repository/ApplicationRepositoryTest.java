package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

/**
 * This a unit test of the Application repository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions

public class ApplicationRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    private Role testRole;

    @Autowired
    private PersonRepository personRepository;
    private Person testPerson;

    @Autowired 
    private CompetenceRepository competenceRepository;
    private Competence competence;

    @Autowired 
    private CompetenceProfileRepository competenceProfileRepository;
    private List<CompetenceProfile> competenceProfiles;

    @Autowired 
    private AvailabilityRepository availabilityRepository;
    private List<Availability> availabilityList;
    private Availability availability;
    private Availability availability2;
    private Availability availability3;


    @Autowired 
    private ApplicationRepository applicationRepository;

    private Application application;
    private Application application2;

    @BeforeEach
    public void setUp() {

        long systemTime=System.currentTimeMillis();

        // Initialize test data before each test method

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

        availabilityList= new ArrayList<Availability>();
        availability = new Availability(testPerson, new java.sql.Date(systemTime+44444), new java.sql.Date(systemTime+44444));
        availability2 = new Availability(testPerson, new java.sql.Date(systemTime+3123124), new java.sql.Date(systemTime+5123123));
        availability3 = new Availability(testPerson, new java.sql.Date(systemTime+3123124), new java.sql.Date(systemTime+5123123));
        availabilityRepository.save(availability);
        availabilityRepository.save(availability2);
        availabilityRepository.save(availability3);

        availabilityList.add(availability);
        availabilityList.add(availability2);
        List<Availability> availabilityList2= new ArrayList<Availability>();
        availabilityList2.add(availability);
        availabilityList2.add(availability3);
    
        competence = new Competence();
        competence.setName("competence");
        competenceRepository.save(competence);

        CompetenceProfile profile = new CompetenceProfile(testPerson, competence, 2.0);
        competenceProfileRepository.save(profile);        
        
        competenceProfiles= new ArrayList<CompetenceProfile>();
        competenceProfiles.add(profile);

        application= new Application(testPerson, availabilityList,competenceProfiles );
        application2= new Application(testPerson, availabilityList2,competenceProfiles );
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

    @Test
    /**
     * This tests the existsByAvailabilityPeriodsForApplicationAndApplicant method
     */
    void existsByAvailabilityPeriodsForApplicationAndApplicantTest()
    {        
        Person tempPerson=new Person();
        tempPerson.setName("test");
        tempPerson.setSurname("testsson");
        tempPerson.setEmail("test@test.test");
        tempPerson.setPassword("testPassword");
        tempPerson.setPnr("12345678-1234");
        tempPerson.setRole(testRole);
        tempPerson.setUsername("username");
        personRepository.save(tempPerson);

        /* TODO add back these
        assertTrue(applicationRepository.existsByAvailabilityPeriodsForApplicationAndApplicant(availabilityList,testPerson));
        assertFalse(applicationRepository.existsByAvailabilityPeriodsForApplicationAndApplicant(new ArrayList<Availability>(),testPerson));
        assertFalse(applicationRepository.existsByAvailabilityPeriodsForApplicationAndApplicant(availabilityList,tempPerson));*/

    }

    @Test
    /** TODO remove this */
    void tempSqlTest()
    {
        List<Integer> integer=new ArrayList<Integer>();
        integer.add(availability.getAvailabilityId());

        List<Integer> countResult=applicationRepository.countOfReusedAvailability(integer.toArray());

        for (Integer i : countResult) {
            System.out.println(i);
            assertEquals(1, i);
        }

        integer.add(1231240);

        countResult=applicationRepository.countOfReusedAvailability(integer.toArray());
        for (Integer i : countResult) {
            assertEquals(1, i);
        }

        integer.add(availability2.getAvailabilityId());

        countResult=applicationRepository.countOfReusedAvailability(integer.toArray());
        for (Integer i : countResult) {
            System.out.println(i);
        }

        assertFalse(applicationRepository.isListFullyReused(integer.toArray(), integer.size()));
        integer.remove(1);
        assertTrue(applicationRepository.isListFullyReused(integer.toArray(), integer.size()));
        integer.add(availability3.getAvailabilityId());
        assertFalse(applicationRepository.isListFullyReused(integer.toArray(), integer.size()));

        integer.remove(availability3.getAvailabilityId());

        assertTrue(applicationRepository.isListFullyReusedForAPerson(integer, integer.size(), testPerson.getId()));

        integer.remove(0);
        assertTrue(applicationRepository.isListFullyReusedForAPerson(integer, integer.size(), testPerson.getId()));


        Person tempPerson=new Person();
        tempPerson.setName("test");
        tempPerson.setSurname("testsson");
        tempPerson.setEmail("test@test.test");
        tempPerson.setPassword("testPassword");
        tempPerson.setPnr("12345678-1234");
        tempPerson.setRole(testRole);
        tempPerson.setUsername("username");
        personRepository.save(tempPerson);

        assertNull(applicationRepository.isListFullyReusedForAPerson(integer, integer.size(), tempPerson.getId()));
        
        long systemTime=System.currentTimeMillis();

        Availability availabilityTemp = new Availability(tempPerson, new java.sql.Date(systemTime+44444), new java.sql.Date(systemTime+44444));
        availabilityRepository.save(availabilityTemp);

        List<Availability> availabilityList2= new ArrayList<Availability>();
        availabilityList2.add(availabilityTemp);
    
        Competence competenceTemp = new Competence();
        competenceTemp.setName("competence");
        competenceRepository.save(competenceTemp);

        CompetenceProfile profile = new CompetenceProfile(tempPerson, competenceTemp, 2.0);
        competenceProfileRepository.save(profile);        
        
        List<CompetenceProfile>competenceProfilesTemp= new ArrayList<CompetenceProfile>();
        competenceProfilesTemp.add(profile);

        application= new Application(tempPerson, availabilityList2,competenceProfilesTemp );

        integer.add(availabilityTemp.getAvailabilityId());
        assertTrue(applicationRepository.isListFullyReusedForAPerson(integer, integer.size(), tempPerson.getId()));

    }

    @Test
    /**
     * This tests the application entities constraints
     */
    void applicationConstraintTest()
    {
        application= new Application(null, availabilityList,competenceProfiles );
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("Each application must be for a specific person"));

        //We then test the availability period constraints
        application= new Application(testPerson, new ArrayList<Availability>() ,competenceProfiles );
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("You must specify at least one availability period"));
        
        application= new Application(testPerson, null ,competenceProfiles );
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("availabilityPeriodsForApplication may not be null"));
        
        availabilityList.add(availabilityList.get(0));

        application= new Application(testPerson, availabilityList ,competenceProfiles );
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("No availability period should be included multiple times in the application"));
        availabilityList.remove(1);

        //We then test the competence profile constraints
        application= new Application(testPerson, availabilityList,new ArrayList<CompetenceProfile>() );
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("You must specify at least one competence profile"));
        
        application= new Application(testPerson, availabilityList,null );
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("competenceProfilesForApplication may not be null"));
        
        competenceProfiles.add(competenceProfiles.get(0));
        application= new Application(testPerson, availabilityList,competenceProfiles);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("No competence profile should be included multiple times in the application"));
        competenceProfiles.remove(0);

        //We then test application status constraint
        application= new Application(testPerson, availabilityList,competenceProfiles);
        application.setApplicationStatus(null);
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("Application status should never be null"));

        //And finally we test application_date constraint
        application= new Application(testPerson, availabilityList,competenceProfiles);
        application.setApplicationDate(new java.sql.Date(System.currentTimeMillis()+10000));
        e = assertThrowsExactly(ConstraintViolationException.class, () -> applicationRepository.save(application));
        assertEquals(true,e.getConstraintViolations().toString().contains("Application date should never be in the future!"));
 
    }
}
