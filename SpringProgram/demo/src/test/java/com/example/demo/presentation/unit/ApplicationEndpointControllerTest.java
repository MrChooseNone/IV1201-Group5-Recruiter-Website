package com.example.demo.presentation.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

import java.sql.Date;
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

import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.CompetenceProfileDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;
import com.example.demo.domain.requestBodies.ApplicationSubmissionRequestBody;
import com.example.demo.presentation.restControllers.ApplicationEndpointController;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.ApplicationService;

@ExtendWith(MockitoExtension.class)
/**
 * This tests the ApplicationEndpointController class
 */
public class ApplicationEndpointControllerTest {
    // This is used to define the service we want to mock
    @Mock
    private ApplicationService applicationService;

    // We then define the controller we want to test
    @InjectMocks
    private ApplicationEndpointController applicationEndpointController;

    private static Authentication authentication;

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {

        Role role = new Role();
        role.setName("testRole");
        Person person = new Person();
        person.setId(0);
        person.setRole(role);
        PersonDetails details=new PersonDetails(person);
        authentication=new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());
        SecurityContext securityContext = Mockito.mock(SecurityContext.class); //https://stackoverflow.com/questions/360520/unit-testing-with-spring-security 
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);//This mocks the security context, with the above authentication
        SecurityContextHolder.setContext(securityContext); //This should ensure above is used whenever security context is accessed

        MockitoAnnotations.openMocks(ApplicationEndpointControllerTest.class);
    }

    @Test
    /**
     * This tests the GetCompetenceProfilesForAPerson method
     */
    void GetCompetenceProfilesForAPersonTest() {
        // We then define the mock implementation for the service function, in this case
        // if a real id is given it returns a list with a fake matching competence
        // profile
        when(applicationService.GetCompetenceProfilesForAPerson(anyInt())).thenAnswer(invocation -> {
            Integer intArg = (Integer) invocation.getArguments()[0];
            Person p = new Person();
            p.setId(intArg);
            CompetenceProfile profile = new CompetenceProfile(p, null, 0);
            List<CompetenceProfile> profileList = new ArrayList<CompetenceProfile>();
            profileList.add(profile);
            return profileList;
        });

        // We first test that it throws the correct exception for an invalid integer
        var e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.GetCompetenceProfilesForAPerson("notAnInteger"));
        assertEquals("Invalid parameter : Provided value (notAnInteger) could not be parsed as a valid integer",
                e.getMessage());

        // And then that it returns the list it recived from service for a valid integer
        List<? extends CompetenceProfileDTO> result = applicationEndpointController
                .GetCompetenceProfilesForAPerson("1");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPerson().getId());
    }

    @Test
    /**
     * This tests the CreateCompetenceProfile method
     */
    void CreateCompetenceProfileTest() {
        // We then define the mock implementation for the service function, in this case
        // if a integer, integer and double is given a fake competence profile is
        // returned
        when(applicationService.CreateCompetenceProfile(anyInt(), anyInt(), anyDouble())).thenAnswer(invocation -> {
            Integer intArg1 = (Integer) invocation.getArguments()[0];
            Competence c = new Competence();
            c.setId(intArg1);

            Integer intArg2 = (Integer) invocation.getArguments()[1];
            Person p = new Person();
            p.setId(intArg2);

            CompetenceProfile profile = new CompetenceProfile(p, c, (Double) invocation.getArguments()[2]);
            return profile;
        });

        // We first test that it throws the correct exception for an invalid integer and
        // double for each of the parameters
        var e = assertThrowsExactly(InvalidParameterException.class, () -> applicationEndpointController
                .CreateCompetenceProfile("notAnInteger", "notAPersonId", "notaDouble"));
        assertEquals("Invalid parameter : Provided value (notAnInteger) could not be parsed as a valid integer",
                e.getMessage());

        e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.CreateCompetenceProfile("0", "notAPersonId", "notaDouble"));
        assertEquals("Invalid parameter : Provided value (notAPersonId) could not be parsed as a valid integer",
                e.getMessage());

        e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.CreateCompetenceProfile("0", "0", "notaDouble"));
        assertEquals("Invalid parameter : Provided value (notaDouble) could not be parsed as a valid double",
                e.getMessage());

        // And then that it returns the list it recived from service for a valid integer
        CompetenceProfileDTO result = applicationEndpointController.CreateCompetenceProfile("1", "2", "3.1");
        assertEquals(1, result.getPerson().getId());
        assertEquals(2, result.getCompetenceDTO().getCompetenceId());
        assertEquals(3.1, result.getYearsOfExperience());

    }

    @Test
    /**
     * This tests the GetAllAvailability method
     */
    void GetAllAvailabilityTest() {
        // We then define the mock implementation for the service function, in this case
        // if a real id is given it returns a list with a fake matching availability
        // period
        when(applicationService.GetAvailabilityForAPerson(anyInt())).thenAnswer(invocation -> {
            Integer intArg = (Integer) invocation.getArguments()[0];
            Person p = new Person();
            p.setId(intArg);
            Availability availability = new Availability(p, null, null);
            List<Availability> availabilityList = new ArrayList<Availability>();
            availabilityList.add(availability);
            return availabilityList;
        });

        // We first test that it throws the correct exception for an invalid integer
        var e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.GetAllAvailability("notAnInteger"));
        assertEquals("Invalid parameter : Provided value (notAnInteger) could not be parsed as a valid integer",
                e.getMessage());

        // And then that it returns the list it recived from service for a valid integer
        List<? extends AvailabilityDTO> result = applicationEndpointController.GetAllAvailability("1");
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getPerson().getId());
    }

    @Test
    /**
     * This tests the CreateAvailability method
     */
    void CreateAvailabilityTest() {
        // We then define the mock implementation for the service function, in this case
        // if a integer, date and date is given a fake availability is returned with
        // those values
        when(applicationService.CreateAvailability(anyInt(), any(Date.class), any(Date.class)))
                .thenAnswer(invocation -> {

                    Integer intArg = (Integer) invocation.getArguments()[0];
                    Person p = new Person();
                    p.setId(intArg);

                    Date dateArg1 = (Date) invocation.getArguments()[1];
                    Date dateArg2 = (Date) invocation.getArguments()[2];

                    Availability availability = new Availability(p, dateArg1, dateArg2);
                    return availability;
                });

        // We first test that it throws the correct exception for invalid integer or
        // date parameters
        var e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.CreateAvailability("notAnInteger", "notAFromDate", "notAToDate"));
        assertEquals("Invalid parameter : Provided value (notAnInteger) could not be parsed as a valid integer",
                e.getMessage());

        e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.CreateAvailability("0", "notAFromDate", "notAToDate"));
        assertEquals(
                "Invalid parameter : Provided value (notAFromDate) could not be parsed as a valid date, please use the yyyy-(m)m-(d)d format, with the (m) and (d) specifying that these can be 0 or ignored",
                e.getMessage());

        e = assertThrowsExactly(InvalidParameterException.class,
                () -> applicationEndpointController.CreateAvailability("0", "2000-01-10", "notAToDate"));
        assertEquals(
                "Invalid parameter : Provided value (notAToDate) could not be parsed as a valid date, please use the yyyy-(m)m-(d)d format, with the (m) and (d) specifying that these can be 0 or ignored",
                e.getMessage());

        // And then that it returns the list it recived from service for a valid integer
        AvailabilityDTO result = applicationEndpointController.CreateAvailability("1", "2000-01-10", "2000-01-14");
        assertEquals(1, result.getPerson().getId());
        assertEquals(true, result.getFromDate().equals(Date.valueOf("2000-01-10")));
        assertEquals(true, result.getToDate().equals(Date.valueOf("2000-01-14")));

    }

    @Test
    /**
     * This tests the SubmitApplication method
     * Note that since a RequestBody is used instead of requestParam the parsing is done by Spring before the method is called, so this is a much shorter test
     * TODO check if there is any better way to test this, perhaps add a "integration" test to check that input parsing is done correctly?
     */
    void SubmitApplicationTest() {
        // We then define the mock implementation for the service function, in this case
        // if a integer, date and date is given a fake availability is returned with
        // those values
        when(applicationService.SubmitApplication(anyInt(), anyList(), anyList())).thenAnswer(invocation -> {
            Integer intArg = (Integer) invocation.getArguments()[0];
            Person p = new Person();
            p.setId(intArg);

            //Note this could throw an exception if the service is passed a list of something other than integers
            @SuppressWarnings("unchecked")
            List<Integer> availabilityIds  = (List<Integer>) invocation.getArguments()[1];

            List<Availability> availabilityListArg = new ArrayList<Availability>();

            for (Integer id : availabilityIds) {
                Availability a = new Availability();
                a.setAvailabilityId(id);
                availabilityListArg.add(a);
            }

            @SuppressWarnings("unchecked")
            List<Integer> competenceProfilesIdsArg = (List<Integer>) invocation.getArguments()[2];

            List<CompetenceProfile> CompetenceProfileList = new ArrayList<CompetenceProfile>();

            for (Integer id : competenceProfilesIdsArg) {
                CompetenceProfile c = new CompetenceProfile();
                c.setCompetenceProfileId(id);
                CompetenceProfileList.add(c);
            }

            Application application = new Application(p, availabilityListArg, CompetenceProfileList);
            return application;
        });

        List<Integer> competenceProfilesIds = new ArrayList<Integer>();
        competenceProfilesIds.add(23);
        List<Integer> availabilityIds = new ArrayList<Integer>(); 
        availabilityIds.add(54);

        //In this case we just test the correct execution, since the endpoint controller has no real logic
        ApplicationSubmissionRequestBody body = new ApplicationSubmissionRequestBody(1, availabilityIds, competenceProfilesIds);

        ApplicationDTO result=applicationEndpointController.SubmitApplication(body);

        assertEquals(1, result.getApplicant().getId());
        assertEquals(1, result.getAvailabilityPeriodsForApplication().size());
        assertEquals(1, result.getCompetenceProfilesForApplication().size());        
        assertEquals(23, result.getCompetenceProfilesForApplication().get(0).getCompetenceProfileId());
        assertEquals(54, result.getAvailabilityPeriodsForApplication().get(0).getAvailabilityId());


    }
}
