package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.TransientDataAccessException;

import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.CompetenceProfileDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.AlreadyExistsException;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.FromDateAfterToDateException;
import com.example.demo.presentation.restException.PeriodAlreadyCoveredException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.AvailabilityInvalidException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.CompetenceProfileInvalidException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.SpecificCompetenceNotFoundException;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.AvailabilityRepository;
import com.example.demo.repository.CompetenceProfileRepository;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.PersonRepository;

@ExtendWith(MockitoExtension.class)
/**
 * This class defined the unit tests for the ApplicationService class
 */
public class ApplicationServiceTest {
    // We first define the repositories to mock
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private CompetenceProfileRepository competenceProfileRepository;
    @Mock
    private AvailabilityRepository availabilityRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private CompetenceRepository competenceRepository;

    // We then define the service we are testing
    @InjectMocks
    private ApplicationService applicationService;

    // We then define the lists we will use as the mock databases
    static List<Application> savedApplications = new ArrayList<Application>();
    static List<CompetenceProfile> savedCompetenceProfiles = new ArrayList<CompetenceProfile>();
    static List<Availability> savedAvailabilities = new ArrayList<Availability>();
    static List<Person> savedPersons = new ArrayList<Person>();
    static List<Competence> savedCompetences = new ArrayList<Competence>();

    // This ensures mocks are created correctly and empty the mock databases
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(ApplicationServiceTest.class);
    }

    // This ensures the mock "databases" are clean after each attempt
    @AfterEach
    public void afterEach() {
        savedApplications.clear();
        savedCompetenceProfiles.clear();
        savedAvailabilities.clear();
        savedPersons.clear();
        savedCompetences.clear();
    }

    @Test
    /**
     * This is a test for the method CreateCompetenceProfile
     */
    void CreateCompetenceProfileTest() {
        // We first create the test object
        Competence competence = new Competence();
        competence.setName("testCompetence");
        competence.setId(0);
        Person person = new Person();
        person.setName("testName");
        person.setId(0);
        Double yearsOfExperience = 1.0;

        // We then define the implementation for the mock repository functions
        when(competenceRepository.save(any(Competence.class))).thenAnswer(invocation -> {
            savedCompetences.add((Competence) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer idArg = (Integer) invocation.getArguments()[0];
            Optional<Competence> cContainer;
            // Link to page regarding Java optional: https://www.baeldung.com/java-optional
            for (Competence c : savedCompetences) {
                if (c.getCompetenceId() == idArg) {
                    cContainer = Optional.of(c);
                    return cContainer;
                }
            }
            cContainer = Optional.empty();
            return cContainer;
        });

        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPersons.add((Person) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer idArg = (Integer) invocation.getArguments()[0];
            Optional<Person> pContainer;
            // Link to page regarding Java optional: https://www.baeldung.com/java-optional
            for (Person c : savedPersons) {
                if (c.getId() == idArg) {
                    pContainer = Optional.of(c);
                    return pContainer;
                }
            }
            pContainer = Optional.empty();
            return pContainer;
        });

        when(competenceProfileRepository.save(any(CompetenceProfile.class))).thenAnswer(invocation -> {
            savedCompetenceProfiles.add((CompetenceProfile) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(any(Person.class),
                any(Competence.class), anyDouble())).thenAnswer(invocation -> {
                    Person personArg = ((Person) invocation.getArguments()[0]);
                    Competence competenceArg = ((Competence) invocation.getArguments()[1]);
                    Double yearsOfExperienceArg = (Double) invocation.getArguments()[2];

                    for (CompetenceProfile c : savedCompetenceProfiles) {
                        if (c.getPerson() == personArg && c.getCompetenceDTO() == competenceArg
                                && c.getYearsOfExperience() == yearsOfExperienceArg) {
                            return true;
                        }
                    }
                    return false;
                });

        // We then test that the method throws the correct exceptions, starting with the
        // exception for if no competence with that id exists, afterwards we add that
        // competence
        var e = assertThrowsExactly(SpecificCompetenceNotFoundException.class, () -> applicationService .CreateCompetenceProfile(competence.getCompetenceId(), person.getId(), yearsOfExperience));
        assertEquals("Could not find specific competence with id : 0", e.getMessage());
        competenceRepository.save(competence);

        // We then test the exception for if a person does not exist, and afterwards add
        // it
        var e2 = assertThrowsExactly(PersonNotFoundException.class, () -> applicationService.CreateCompetenceProfile(competence.getCompetenceId(), person.getId(), yearsOfExperience));
        assertEquals("Could not find a person with the following id : 0", e2.getMessage());
        personRepository.save(person);

        // We then test if the method can add a competence profile correctly
        assertEquals(false, competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(person,competence, yearsOfExperience));
        CompetenceProfileDTO result = applicationService.CreateCompetenceProfile(competence.getCompetenceId(),person.getId(), yearsOfExperience);
        assertEquals(true, competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(person,competence, yearsOfExperience));

        assertNotNull(result);
        assertEquals(person, result.getPerson());
        assertEquals(competence, result.getCompetenceDTO());

        // And finally we confirm that duplicate profiles can not exist
        var e3 = assertThrowsExactly(AlreadyExistsException.class, () -> applicationService.CreateCompetenceProfile(competence.getCompetenceId(), person.getId(), yearsOfExperience));
        assertEquals("The requested resource already exists : This competence profile already exists, so it does not need to be created",e3.getMessage());

        //We then test that it handles database exceptions correctly

        // We then define the implementation for the mock repository functions
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(personRepository).findById(anyInt());

        savedApplications.clear(); //We clear the application database, which should allow this application to be submitted again

        var e4 = assertThrowsExactly(CustomDatabaseException.class, () -> applicationService.CreateCompetenceProfile(competence.getCompetenceId(), person.getId(), yearsOfExperience));
        assertEquals("Failed due to database error, please try again",e4.getMessage());

    }

    @Test
    /**
     * This is a test for the method GetCompetenceProfilesForAPerson
     */
    void GetCompetenceProfilesForAPersonTest() {
        // We first create the test object
        Competence competence = new Competence();
        competence.setName("testCompetence");
        competence.setId(0);
        Person person = new Person();
        person.setName("testName");
        person.setId(0);
        Double yearsOfExperience = 1.0;
        CompetenceProfile profile = new CompetenceProfile(person, competence, yearsOfExperience);

        // We then define the implementation for the mock repositories
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPersons.add((Person) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findById(anyInt())).thenAnswer(invocation -> {

            Integer idArg = (Integer) invocation.getArguments()[0];

            Optional<Person> tContainer;
            for (Person p : savedPersons) {
                if (p.getId() == idArg) {
                    tContainer = Optional.of(p);
                    return tContainer;
                }
            }
            tContainer = Optional.empty();
            return tContainer;
        });

        when(competenceProfileRepository.save(any(CompetenceProfile.class))).thenAnswer(invocation -> {
            savedCompetenceProfiles.add((CompetenceProfile) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceProfileRepository.findAllByPerson(any(Person.class))).thenAnswer(invocation -> {
            Person personArg = (Person) invocation.getArguments()[0];
            List<CompetenceProfile> competenceProfiles = new ArrayList<CompetenceProfile>();
            for (CompetenceProfile c : savedCompetenceProfiles) {
                if (c.getPerson() == personArg) {
                    competenceProfiles.add(c);
                }
            }
            return competenceProfiles;
        });

        // We then test that the PersonNotFoundException is thrown if the person does
        // not exist
        var e = assertThrowsExactly(PersonNotFoundException.class,() -> applicationService.GetCompetenceProfilesForAPerson(person.getId()));
        assertEquals("Could not find a person with the following id : 0", e.getMessage());
        personRepository.save(person);

        // We then test that it returns an empty list when the person exists but has no
        // competence profiles
        List<? extends CompetenceProfileDTO> result = applicationService
                .GetCompetenceProfilesForAPerson(person.getId());
        assertEquals(0, result.size());

        // And finally we test that if we add a profile for a person it is returned when
        // we search for it
        competenceProfileRepository.save(profile);
        result = applicationService.GetCompetenceProfilesForAPerson(person.getId());
        assertEquals(1, result.size());
        assertEquals(profile, result.get(0));

        //We then test that it handles database exceptions correctly

        // We then define the implementation for the mock repository functions
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(personRepository).findById(anyInt());

        var e4 = assertThrowsExactly(CustomDatabaseException.class, () -> applicationService.GetCompetenceProfilesForAPerson(person.getId()));
        assertEquals("Failed due to database error, please try again",e4.getMessage());

    }

    @Test
    /**
     * This is a test for the method CreateAvailability
     */
    void CreateAvailabilityTest() {

        // We first create the test object
        Person person = new Person();
        person.setName("testName");
        person.setId(0);
        Date fromDate = Date.valueOf("2000-12-1");
        Date fromDateConflict = Date.valueOf("2001-02-1");
        Date toDate = Date.valueOf("2001-12-1");

        Availability availability = new Availability(person, fromDate, toDate);

        // We then define the implementation for the mock repositories

        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPersons.add((Person) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findById(anyInt())).thenAnswer(invocation -> {

            Integer idArg = (Integer) invocation.getArguments()[0];

            Optional<Person> tContainer;
            for (Person p : savedPersons) {
                if (p.getId() == idArg) {
                    tContainer = Optional.of(p);
                    return tContainer;
                }
            }
            tContainer = Optional.empty();
            return tContainer;
        });

        when(availabilityRepository.save(any(Availability.class))).thenAnswer(invocation -> {
            savedAvailabilities.add((Availability) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(availabilityRepository.existsByFromDateAndToDateAndPerson(any(Date.class), any(Date.class),
                any(Person.class))).thenAnswer(invocation -> {
                    Date dateArg1 = (Date) invocation.getArguments()[0];
                    Date dateArg2 = (Date) invocation.getArguments()[1];
                    Person personArg = (Person) invocation.getArguments()[2];
                    for (Availability a : savedAvailabilities) {
                        if (a.getPerson() == personArg && a.getFromDate() == dateArg1 && a.getToDate() == dateArg2) {
                            return true;
                        }
                    }
                    return false;
                });

        when(availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(any(Date.class),
                any(Date.class), any(Person.class))).thenAnswer(invocation -> {
                    Date dateArg1 = (Date) invocation.getArguments()[0];
                    Date dateArg2 = (Date) invocation.getArguments()[1];
                    Person personArg = (Person) invocation.getArguments()[2];
                    for (Availability a : savedAvailabilities) {
                        if (a.getPerson() == personArg
                                && (a.getFromDate().before(dateArg1) || a.getFromDate() == dateArg1)
                                && (a.getToDate().after(dateArg2) || a.getToDate() == dateArg2)) {
                            return true;
                        }
                    }
                    return false;
                });

        // We then test the exception for if a person does not exist, and afterwards add
        // it
        var e = assertThrowsExactly(PersonNotFoundException.class, () -> applicationService
                .CreateAvailability(person.getId(), availability.getFromDate(), availability.getToDate()));
        assertEquals("Could not find a person with the following id : 0", e.getMessage());
        personRepository.save(person);

        var e2 = assertThrowsExactly(FromDateAfterToDateException.class, () -> applicationService
                .CreateAvailability(person.getId(), availability.getToDate(), availability.getFromDate()));
        assertEquals("Could not create availability period since start date 2001-12-01 is after end date 2000-12-01",
                e2.getMessage());

        AvailabilityDTO results = applicationService.CreateAvailability(person.getId(), availability.getFromDate(),
                availability.getToDate());

        assertNotNull(results);
        assertEquals(person, results.getPerson());
        assertEquals(fromDate, results.getFromDate());
        assertEquals(toDate, results.getToDate());

        var e3 = assertThrowsExactly(AlreadyExistsException.class, () -> applicationService.CreateAvailability(person.getId(), availability.getFromDate(), availability.getToDate()));
        assertEquals(
                "The requested resource already exists : This availability period already exists, so it does not need to be created",
                e3.getMessage());

        // And finally we confirm that duplicate profiles can not exist
        var e4 = assertThrowsExactly(PeriodAlreadyCoveredException.class, () -> applicationService
                .CreateAvailability(person.getId(), fromDateConflict, availability.getToDate()));
        assertEquals(
                "Could not create availability period since range start date 2001-02-01 to end date 2001-12-01 is fully covered by an existing availability period ",
                e4.getMessage());

        //We then test that it handles database exceptions correctly

        // We then define the implementation for the mock repository functions
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(personRepository).findById(anyInt());

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> applicationService.CreateAvailability(person.getId(), availability.getFromDate(), availability.getToDate()));
        assertEquals("Failed due to database error, please try again",e5.getMessage());


    }

    @Test
    /**
     * This is a test for the method GetAvailabilityForAPerson
     */
    void GetAvailabilityForAPersonTest() {

        // We first create the test object
        Person person = new Person();
        person.setName("testName");
        person.setId(0);
        Availability availability = new Availability(person, new java.sql.Date(System.currentTimeMillis() - 1),
                new java.sql.Date(System.currentTimeMillis()));

        // We then define the implementation for the mock repositories
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPersons.add((Person) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findById(anyInt())).thenAnswer(invocation -> {

            Integer idArg = (Integer) invocation.getArguments()[0];

            Optional<Person> tContainer;
            for (Person p : savedPersons) {
                if (p.getId() == idArg) {
                    tContainer = Optional.of(p);
                    return tContainer;
                }
            }
            tContainer = Optional.empty();
            return tContainer;
        });

        when(availabilityRepository.save(any(Availability.class))).thenAnswer(invocation -> {
            savedAvailabilities.add((Availability) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(availabilityRepository.findAllByPerson(any(Person.class))).thenAnswer(invocation -> {
            Person personArg = (Person) invocation.getArguments()[0];
            List<Availability> availabilityResult = new ArrayList<Availability>();
            for (Availability a : savedAvailabilities) {
                if (a.getPerson() == personArg) {
                    availabilityResult.add(a);
                }
            }
            return availabilityResult;
        });

        // We first test that the PersonNotFoundException is thrown if the person does not exist
        var e = assertThrowsExactly(PersonNotFoundException.class,
                () -> applicationService.GetCompetenceProfilesForAPerson(person.getId()));
        assertEquals("Could not find a person with the following id : 0", e.getMessage());
        personRepository.save(person);

        // We then test that it returns an empty list when the person exists but has no
        // availability periods
        List<? extends AvailabilityDTO> result = applicationService.GetAvailabilityForAPerson(person.getId());
        assertEquals(0, result.size());

        // We finally test that after saving a availability period it is returned
        // correctly
        availabilityRepository.save(availability);
        result = applicationService.GetAvailabilityForAPerson(person.getId());
        assertEquals(1, result.size());
        assertEquals(availability, result.get(0));

        //We then test that it handles database exceptions correctly

        // We then define the implementation for the mock repository functions
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(personRepository).findById(anyInt());

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> applicationService.GetAvailabilityForAPerson(person.getId()));
        assertEquals("Failed due to database error, please try again",e5.getMessage());

    }

    @Test
    /**
     * This is a test for the method SubmitApplication
     */
    void SubmitApplication() {

        // We first create the test object
        Person person = new Person();
        person.setName("testName");
        person.setId(0);
        
        Person person2 = new Person();
        person2.setName("testName2");
        person2.setId(1);

        Availability availability = new Availability(person, new java.sql.Date(System.currentTimeMillis() - 1), new java.sql.Date(System.currentTimeMillis()));
        availability.setAvailabilityId(0);

        Availability availability2 = new Availability(person2, new java.sql.Date(System.currentTimeMillis() - 1), new java.sql.Date(System.currentTimeMillis()));
        availability2.setAvailabilityId(1);

        Competence competence = new Competence();
        competence.setName("testCompetence");
        competence.setId(0);

        Double yearsOfExperience = 1.0;

        CompetenceProfile profile = new CompetenceProfile(person, competence, yearsOfExperience);
        profile.setCompetenceProfileId(0); 

        CompetenceProfile profile2 = new CompetenceProfile(person2, competence, yearsOfExperience);
        profile2.setCompetenceProfileId(1);

        List<Integer> availabilityIds=new ArrayList<Integer>();
        List<Integer> competenceProfileIds=new ArrayList<Integer>();


        // We then define the implementation for the mock repositories
        when(personRepository.save(any(Person.class))).thenAnswer(invocation -> {
            savedPersons.add((Person) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(personRepository.findById(anyInt())).thenAnswer(invocation -> {

            Integer idArg = (Integer) invocation.getArguments()[0];
            Optional<Person> tContainer;
            for (Person p : savedPersons) {
                if (p.getId() == idArg) {
                    tContainer = Optional.of(p);
                    return tContainer;
                }
            }
            tContainer = Optional.empty();
            return tContainer;
        });
      
        when(competenceProfileRepository.save(any(CompetenceProfile.class))).thenAnswer(invocation -> {
            savedCompetenceProfiles.add((CompetenceProfile) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

  
        when(competenceProfileRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer idArg = (Integer) invocation.getArguments()[0];
            Optional<CompetenceProfile> cContainer;
            for (CompetenceProfile c : savedCompetenceProfiles) {
                if (c.getCompetenceProfileId() == idArg) {
                    cContainer = Optional.of(c);
                    return cContainer;
                }
            }
            cContainer = Optional.empty();
            return cContainer;
        });

        when(availabilityRepository.save(any(Availability.class))).thenAnswer(invocation -> {
            savedAvailabilities.add((Availability) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(availabilityRepository.findById(anyInt())).thenAnswer(invocation -> {

            Integer idArg = (Integer) invocation.getArguments()[0];
            Optional<Availability> aContainer;
            for (Availability a : savedAvailabilities) {
                if (a.getAvailabilityId() == idArg) {
                    aContainer = Optional.of(a);
                    return aContainer;
                }
            }
            aContainer = Optional.empty();
            return aContainer;
        });

        //TODO fix this
        when(applicationRepository.isListFullyReusedForAPerson(anyList(),anyInt(),anyInt())).thenAnswer(invocation -> {

            List<Integer> listArg = (List<Integer>) invocation.getArguments()[0];
            Integer personIdArg = (Integer) invocation.getArguments()[2];

            Boolean status=true;
            for (Application a : savedApplications) {
                if (a.getApplicant().getId()==personIdArg) {

                    List<Availability> availabilitiesList=a.getAvailabilityPeriodsForApplication();
                    if (availabilitiesList.size()>=listArg.size()) {
                        for (int index = 0; index < listArg.size(); index++) {
                            if (!listArg.contains(availabilitiesList.get(index).getAvailabilityId())) {
                                status=false;
                                index=listArg.size();
                            }
                        }
                    }
                }
            }
            return false;
        });

        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            savedApplications.add((Application) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        //We then start testing the method to be tested, starting with testing that it throws the correct exceptions

        // We first test that the PersonNotFoundException is thrown if the person does not exist
        var e = assertThrowsExactly(PersonNotFoundException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Could not find a person with the following id : 0", e.getMessage());
        personRepository.save(person);

        //We then test that the AvailabilityInvalidException is thrown for the relevant cases

        //First is if the availabilityId list is empty
        var e2 = assertThrowsExactly(AvailabilityInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Availability invalid due to : No availability period was specified, please specify at least one for this application", e2.getMessage());

        //Then is if the given availability id does not exist in the database
        availabilityIds.add(0);
        e2 = assertThrowsExactly(AvailabilityInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Availability invalid due to : No availability with id 0 in the database", e2.getMessage());

        //And the final case for this exception is for if a provided availability belongs to another user
        availabilityRepository.save(availability);
        availabilityRepository.save(availability2);
        availabilityIds.add(1);
        e2 = assertThrowsExactly(AvailabilityInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Availability invalid due to : The availability period with id 1 belongs to another user", e2.getMessage());

        //Here we remove the invalid id, since otherwise the above exception will be thrown
        availabilityIds.remove(1);

        //We then test that the CompetenceProfileInvalidException is thrown for the relevant cases

        //First is if the competenceProfileIds list is empty
        var e3 = assertThrowsExactly(CompetenceProfileInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Competence profile invalid due to : No competence profile was specified, please specify at least one for this application", e3.getMessage());

        //Then is if the given competence profile id does not exist in the database
        competenceProfileIds.add(0);
        e3 = assertThrowsExactly(CompetenceProfileInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Competence profile invalid due to : No competence profile with id 0 in the database", e3.getMessage());

        //And the final case for this exception is for if a provided competence profile belongs to another user
        competenceProfileRepository.save(profile);
        competenceProfileRepository.save(profile2);
        competenceProfileIds.add(1);
        e3 = assertThrowsExactly(CompetenceProfileInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Competence profile invalid due to : The competence profile with id 1 belongs to another user", e3.getMessage());

        //Here we remove the invalid id, since otherwise the above exception will be thrown
        competenceProfileIds.remove(1);

        //We then test that the correct execution has the correct result
        ApplicationDTO result=applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds);
        assertEquals(profile, result.getCompetenceProfilesForApplication().get(0));
        assertEquals(availability, result.getAvailabilityPeriodsForApplication().get(0));
        assertEquals(person, result.getApplicant());

        //We verify that the mock save method was called by the SubmitApplication method
        Mockito.verify(this.applicationRepository, Mockito.times(1)).save(any(Application.class));

        e2 = assertThrowsExactly(AvailabilityInvalidException.class,() -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Availability invalid due to : You already have an application with the exact same availability period(s)", e2.getMessage());

        //And finally we verify that it saved the correct application
        assertEquals(result.getApplicationId(), savedApplications.get(0).getApplicationId());
        assertEquals(person, savedApplications.get(0).getApplicant());

        //We then test that it handles database exceptions correctly
        // We then define the implementation for the mock repository functions
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(personRepository).findById(anyInt());

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> applicationService.SubmitApplication(person.getId(),availabilityIds,competenceProfileIds));
        assertEquals("Failed due to database error, please try again",e5.getMessage());

    }
}
