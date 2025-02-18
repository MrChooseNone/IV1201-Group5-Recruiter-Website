package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

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

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.ApplicationNotUpdatedException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.ApplicationNotFoundException;
import com.example.demo.repository.ApplicationRepository;

@ExtendWith(MockitoExtension.class)
/**
 * This class defined the unit tests for the ReviewService class
 */
public class ReviewServiceTest {
    // We define the repository to mock
    @Mock
    private ApplicationRepository applicationRepository;

    // We also define the service we will test, along with ensuring the mocked
    // repository is used instead of the real repository
    @InjectMocks
    private ReviewService reviewService;

    // We then define the list we will use as the mock database
    static List<Application> savedApplications = new ArrayList<Application>();

    // This ensures mocks are created correctly and empty the mock databases
    @BeforeAll
    public static void beforeAll() {
        savedApplications=new ArrayList<Application>();
        MockitoAnnotations.openMocks(ReviewServiceTest.class);
    }

    //This ensures the mock "databases" are clean after each attempt
    @AfterEach
    public void afterEach()
    {
        savedApplications.clear();
    }

    @Test
    /**
     * This test is for the GetApplications method
     */
    public void GetApplicationsTest() {
        // We define the test objects
        Application application = new Application();
        application.setApplicationStatus(ApplicationStatus.unchecked);
        Person applicant = new Person();
        applicant.setName("testsson");
        application.setApplicant(applicant);

        // We define the implementation for the mock repository
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            savedApplications.add((Application) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(applicationRepository.findAll()).thenAnswer(invocation -> {
            return savedApplications;
        });

        // We then call the method we are testing, and confirm that is calls the correct mock method
        List<? extends ApplicationDTO> results = reviewService.GetApplications();
        Mockito.verify(this.applicationRepository, Mockito.times(1)).findAll();

        // We then test that the results are as expected, aka empty
        assertNotNull(results);
        assertEquals(0, results.size());

        // We then manually create an application
        applicationRepository.save(application);

        // We then call the method we are testing again, and confirm that is calls the correct mock method
        results = reviewService.GetApplications();
        Mockito.verify(this.applicationRepository, Mockito.times(2)).findAll();
        
        //We then test the results are as expected, aka only 1 application
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(application, results.get(0));
    }

    @Test
    /**
     * This test is for the GetApplicationsByStatus method
     */
    public void GetApplicationsByStatusTest() {
        // We define the test objects
        Application application = new Application();
        application.setApplicationStatus(ApplicationStatus.unchecked);
        Person applicant = new Person();
        applicant.setName("testsson");
        application.setApplicant(applicant);

        // We define the implementation for the mock repository
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            savedApplications.add((Application) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(applicationRepository.findAllByApplicationStatus(any(ApplicationStatus.class))).thenAnswer(invocation -> {
            ApplicationStatus status=(ApplicationStatus)invocation.getArguments()[0];
            List<Application> applications=new ArrayList<Application>();
            for (Application a : savedApplications) {
                if (a.getApplicationStatus()==status) {
                    applications.add(a);
                }
            }
            return applications;
        });

        // We then call the method we are testing, and confirm that is calls the correct mock method
        List<? extends ApplicationDTO> results =reviewService.GetApplicationsByStatus(ApplicationStatus.unchecked);
        Mockito.verify(this.applicationRepository, Mockito.times(1)).findAllByApplicationStatus(any(ApplicationStatus.class));

        // We then test that the results are as expected, aka empty
        assertNotNull(results);
        assertEquals(0, results.size());

        // We then manually create an application
        applicationRepository.save(application);

        results = reviewService.GetApplicationsByStatus(ApplicationStatus.unchecked);
        Mockito.verify(this.applicationRepository, Mockito.times(2)).findAllByApplicationStatus(any(ApplicationStatus.class));

        //We then test the results are as expected, aka only 1 application
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(application, results.get(0));

        // We then call the method we are testing again, and confirm that is calls the correct mock method
        results = reviewService.GetApplicationsByStatus(ApplicationStatus.accepted);
        Mockito.verify(this.applicationRepository, Mockito.times(3)).findAllByApplicationStatus(any(ApplicationStatus.class));
        
        //We then test the results are as expected, aka only 0 applications
        assertNotNull(results);
        assertEquals(0, results.size());
    }

    @Test
    /**
     * This is a test for the SetApplicationStatus method
     */
    void SetApplicationStatusTest()
    {
        // We define the test objects
        Application application = new Application();
        application.setApplicationStatus(ApplicationStatus.unchecked);
        application.setApplicationId(0);
        Person applicant = new Person();
        applicant.setName("testsson");
        application.setApplicant(applicant);

        // We define the implementation for the mock repository
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> {
            savedApplications.add((Application) invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(applicationRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer id=(Integer)invocation.getArguments()[0];
            Optional<Application> tContainer;
            for (Application a : savedApplications) {
                if (a.getApplicationId()==id) {
                    tContainer=Optional.of(a);
                    return tContainer;
                }
            }
            tContainer=Optional.empty();
            return tContainer;
        });

        // We then call the method we are testing, and confirm that is calls the correct mock method, along with throwing the correct exception
        var e=assertThrowsExactly(ApplicationNotFoundException.class, ()->reviewService.SetApplicationStatus(application.getApplicationId(),ApplicationStatus.unchecked,0));
        assertEquals("Could not find any matching application due to : No application with id : \"0\" found", e.getMessage());

        Mockito.verify(this.applicationRepository, Mockito.times(1)).findById(anyInt());

        // We then manually create an application
        applicationRepository.save(application);

        //We then test the different exception
        e=assertThrowsExactly(ApplicationNotFoundException.class, ()->reviewService.SetApplicationStatus(application.getApplicationId(),ApplicationStatus.unchecked,-1));
        assertEquals("Could not find any matching application due to : Unable to update application since someone else updated it since you last retrived it", e.getMessage());
        Mockito.verify(this.applicationRepository, Mockito.times(2)).findById(anyInt());

        var e2=assertThrowsExactly(ApplicationNotUpdatedException.class, ()->reviewService.SetApplicationStatus(application.getApplicationId(),ApplicationStatus.unchecked,0));
        Mockito.verify(this.applicationRepository, Mockito.times(3)).findById(anyInt());
        assertEquals("Could not update application due to : application status is already unchecked", e2.getMessage());

        ApplicationDTO result=reviewService.SetApplicationStatus(application.getApplicationId(),ApplicationStatus.accepted,0);
        Mockito.verify(this.applicationRepository, Mockito.times(4)).findById(anyInt());


        //We then test the results are as expected, aka that the returned application is correct and updated
        assertNotNull(result);
        assertEquals(applicant, result.getApplicant());
        assertEquals(application.getApplicationId(), result.getApplicationId());
        assertEquals(ApplicationStatus.accepted, result.getApplicationStatus());

    }
}
