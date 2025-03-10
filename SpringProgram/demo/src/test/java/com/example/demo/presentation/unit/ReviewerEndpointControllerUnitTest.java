package com.example.demo.presentation.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;
import com.example.demo.presentation.restControllers.ReviewerEndpointController;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.ReviewService;

@ExtendWith(MockitoExtension.class)
public class ReviewerEndpointControllerUnitTest {
    // This is used to define the service we want to mock
    @Mock
    private ReviewService reviewService;

    // We then define the controller we want to test, and state we want to inject
    // the above defined mock object instead of the real one
    @InjectMocks
    private ReviewerEndpointController reviewerEndpointController;

    //This creates a fake authentication object, to allow methods to work correctly despite authentication not really having been performed
    private static Person person;
    private static PersonDetails details;
    private static Authentication authentication;

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        Role role = new Role();
        role.setName("testRole");
        person = new Person();
        person.setId(0);
        person.setRole(role);
        details=new PersonDetails(person);
        authentication=new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class); //https://stackoverflow.com/questions/360520/unit-testing-with-spring-security 
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);//This mocks the security context, with the above authentication
        SecurityContextHolder.setContext(securityContext); //This should ensure above is used whenever security context is accessed
        MockitoAnnotations.openMocks(ReviewerEndpointControllerUnitTest.class);
    }

    @Test
    /**
     * This is a test for the GetApplications method
     */
    void GetApplicationsTest()
    {
        //We create test objects
        List<Application> applications=new ArrayList<Application>();
        applications.add(new Application());
        applications.add(new Application());
        applications.add(new Application());

        //We define the mock objects behaviour
        when(reviewService.GetApplications()).thenAnswer(invocation -> {
            return applications;
        });

        //We then test that the method does return the list it recives from the service
        List<? extends ApplicationDTO> result=reviewerEndpointController.GetApplications();
        assertEquals(applications, result);
        Mockito.verify(this.reviewService, Mockito.times(1)).GetApplications();

    }

    @Test
    /**
     * This is a test for the GetApplications method
     */
    void GetApplicationsByStatus()
    {
        //We create test objects
        List<Application> applications=new ArrayList<Application>();
        Application application1=new Application();
        application1.setApplicationStatus(ApplicationStatus.unchecked);
        applications.add(application1);

        //We define the mock objects behaviour
        when(reviewService.GetApplicationsByStatus(any(ApplicationStatus.class))).thenAnswer(invocation -> {
            ApplicationStatus ApplicationStatusArg=(ApplicationStatus)invocation.getArguments()[0];
            List<Application> returnList=new ArrayList<Application>();

            for (Application a : applications) {
                if (a.getApplicationStatus()==ApplicationStatusArg) {
                    returnList.add(a);
                }
            }

            return returnList;
        });

        var e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.GetApplicationsByStatus("notAStatus"));
        assertEquals("Invalid parameter : Provided value (notAStatus) is not valid value for application status, please specify as \"unchecked\",\"accepted\" or \"denied\"", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).GetApplicationsByStatus(any(ApplicationStatus.class));


        e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.GetApplicationsByStatus(null));
        assertEquals("Invalid parameter : Provided status value is null, please specify as \"unchecked\",\"accepted\" or \"denied\"", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).GetApplicationsByStatus(any(ApplicationStatus.class));


        //We then test that the method does return the list it recives from the service and that this list is correct for the application status
        List<? extends ApplicationDTO> result=reviewerEndpointController.GetApplicationsByStatus("unchecked");
        assertEquals(true, result.contains(application1));
        Mockito.verify(this.reviewService, Mockito.times(1)).GetApplicationsByStatus(any(ApplicationStatus.class));


        //Here we confirm that a valid but unrepresented status results in an empty list
        result=reviewerEndpointController.GetApplicationsByStatus("accepted");
        assertEquals(0, result.size());
        Mockito.verify(this.reviewService, Mockito.times(2)).GetApplicationsByStatus(any(ApplicationStatus.class));
    }

    @Test
    /**
     * This tests the GetApplicationsById method
     */
    void GetApplicationsByIdTest()
    {

        var e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.GetApplicationsById("NotAnInt"));
        assertEquals("Invalid parameter : Provided value (NotAnInt) could not be parsed as a valid integer", e.getMessage());
        reviewerEndpointController.GetApplicationsById("1");
    }

    @Test
    /**
     * This is a test for the UpdateApplicationsByStatus method
     */
    void UpdateApplicationsByStatusTest()
    {
        //We create the test objects
        List<Application> applications=new ArrayList<Application>();
        Application application1=new Application();
        application1.setApplicationStatus(ApplicationStatus.unchecked);
        application1.setApplicationId(0);
        applications.add(application1);

        //We define the mock objects behaviour, in this case we simply set the status of the test object to the provided value
        when(reviewService.SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt())).thenAnswer(invocation -> {
            ApplicationStatus ApplicationStatusArg=(ApplicationStatus)invocation.getArguments()[1];
            application1.setApplicationStatus(ApplicationStatusArg);
            return application1;
        });

        //We then test that the different invalid parameter exception cases are thrown correctly
        var e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.UpdateApplicationsByStatus("0","notAStatus","0"));
        assertEquals("Invalid parameter : Provided value (notAStatus) is not valid value for application status, please specify as \"unchecked\",\"accepted\" or \"denied\"", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt());

        e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.UpdateApplicationsByStatus("0",null,"0"));
        assertEquals("Invalid parameter : Provided status value is null, please specify as \"unchecked\",\"accepted\" or \"denied\"", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt());
        
        e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.UpdateApplicationsByStatus("notANumber","accepted","0"));
        assertEquals("Invalid parameter : Provided value (notANumber) could not be parsed as a valid integer", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt());

        e = assertThrowsExactly(InvalidParameterException.class, () -> reviewerEndpointController.UpdateApplicationsByStatus("0","accepted","notAVersionNumber"));
        assertEquals("Invalid parameter : Provided value (notAVersionNumber) could not be parsed as a valid integer", e.getMessage());
        Mockito.verify(this.reviewService, Mockito.times(0)).SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt());

        //And finally we verify that a correct input results in a correct update of the status
        ApplicationDTO result=reviewerEndpointController.UpdateApplicationsByStatus("0","accepted","0");
        Mockito.verify(this.reviewService, Mockito.times(1)).SetApplicationStatus(anyInt(),any(ApplicationStatus.class),anyInt());
        assertEquals(application1.getApplicationId(), result.getApplicationId());
        assertEquals(ApplicationStatus.accepted, result.getApplicationStatus());


    }

}
