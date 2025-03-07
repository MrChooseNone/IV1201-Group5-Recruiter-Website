package com.example.demo.presentation.restControllers;

import java.sql.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.CompetenceProfileDTO;
import com.example.demo.domain.requestBodies.ApplicationSubmissionRequestBody;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.ApplicationService;

@RestController
@RequestMapping("/application")
@PreAuthorize("hasAuthority('applicant')")
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}") // This uses the config in config/WebConfig.java to allow cross-origin access
/**
 * This endpoint controller is responsible for handeling the requests concerning submitting an application.
 * This includes, for example, creating a new competence profile for a specific user along with submitting an application
 */
public class ApplicationEndpointController {
        @Autowired
    private final ApplicationService applicationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewerEndpointController.class.getName()); 

    /**
     * Constructs a new instance of the ApplicationEndpointController (this is Spring boot managed).
     * 
     * @param applicationService The service used to handle application related manners.
     */
    public ApplicationEndpointController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    /**
     * This function returns a list of competence profiles for a specific person
     * @param personId The person for which the competence profiles is searched for
     * @throws InvalidParameterException this is thrown is a parameter has the incorrect formatting for the data type
     * @return If no exception is thrown, the list of competence profiles
     */
    @GetMapping("/getAllCompetenceProfiles")
    public List<? extends CompetenceProfileDTO> GetCompetenceProfilesForAPerson(@RequestParam String personId)
    {
        LOGGER.info("List of competence profiles for person (`{}`) requested",personId); //TODO add authentication info here, aka who accessed this

        Integer parsedPersonId=null;
        try {
            parsedPersonId=Integer.parseInt(personId);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to retrive a list of competence profiles for person (`{}`) since person id is not a valid integer",personId);
            throw new InvalidParameterException("Provided value ("+personId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            LOGGER.error("Failed to retrive a list of competence profiles for person (`{}`) due to unknown error related to person id",personId);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the person id parameter");
        }

        return applicationService.GetCompetenceProfilesForAPerson(parsedPersonId);
    }

    /**
     * This function creates a new competence profile based on provided info regarding personId, competenceId and years of experience
     * 
     * @param personId This is the id for the person this profile is for, //TODO remove this when auth is done
     * @param competenceId This is the competence this profile was created for
     * @param yearsOfExperience This is the number of years of experience the person claims to have
     * @throws InvalidParameterException this is thrown is a parameter has the incorrect formatting for the data type
     * @return If no exception is thrown, this will return the newly create competence profile as an object
     */
    //TODO maybe remove personId and replace it with accessing this information from the authentication?
    @PostMapping("/createCompetenceProfile")
    public CompetenceProfileDTO CreateCompetenceProfile(@RequestParam String personId,@RequestParam String competenceId,@RequestParam String yearsOfExperience)
    {
        LOGGER.info("Creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience requested",personId,competenceId,yearsOfExperience); //TODO add authentication info here, aka who accessed this

        Integer parsedPersonId=null;
        try {
            parsedPersonId=Integer.parseInt(personId);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience since person id is invalid integer",personId,competenceId,yearsOfExperience);
            throw new InvalidParameterException("Provided value ("+personId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            LOGGER.error("Failed creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience unknown error related to person id",personId);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the person id parameter");
        }

        Integer parsedCompetenceId=null;
        try {
            parsedCompetenceId=Integer.parseInt(competenceId);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience since competence id is invalid integer",personId,competenceId,yearsOfExperience);
            throw new InvalidParameterException("Provided value ("+competenceId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            LOGGER.error("Failed creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience due to unknown error related to competence id",personId,competenceId,yearsOfExperience);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the competence id parameter");
        }

        Double parsedYearsOfExperience=null;
        try {
            parsedYearsOfExperience=Double.parseDouble(yearsOfExperience);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to retrive competence with id (`{}`) since that is invalid integer",yearsOfExperience);
            throw new InvalidParameterException("Provided value ("+yearsOfExperience+") could not be parsed as a valid double" );
        }
        catch(Exception e) {
            LOGGER.error("Failed creation of competence profile for user with id (`{}`) for competence with id (`{}`) with (`{}`) years of experience due to unknown error related to years of experience ",personId,competenceId,yearsOfExperience);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the yearsOfExperience parameter");
        }

        return applicationService.CreateCompetenceProfile(parsedCompetenceId, parsedPersonId, parsedYearsOfExperience);
    }


    /**
     * This function returns a list of a person's availability periods
     * @param personId The person id for the person to find availability periods for
     * @throws InvalidParameterException this is thrown is a parameter has the incorrect formatting for the data type
     * @return If no exception is thrown, a list of availability periods for that person is returned
     */
    @GetMapping("/getAllAvailability")
    public List<? extends AvailabilityDTO> GetAllAvailability(@RequestParam String personId)
    {
        LOGGER.info("List of availability periods for person (`{}`) requested",personId); //TODO add authentication info here, aka who accessed this

        Integer parsedPersonId=null;
        try {
            parsedPersonId=Integer.parseInt(personId);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to retrive a list of availability periods for person (`{}`) since person id is not a valid integer",personId);
            throw new InvalidParameterException("Provided value ("+personId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            LOGGER.error("Failed to retrive a list of availability periods for person (`{}`) due to unknown error related to person id",personId);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the person id parameter");
        }

        return applicationService.GetAvailabilityForAPerson(parsedPersonId);
    }

    /**
     * This function creates a new availability period for a specific person
     * @param personId The person for to create a availability period for
     * @param fromDate The start of this availability period
     * @param toDate The end of this availability period
     * @throws InvalidParameterException this is thrown is a parameter has the incorrect formatting for the data type
     * @return If no exception is thrown, the newly created availability period is returned
     */
    @PostMapping("/createAvailability")
    public AvailabilityDTO CreateAvailability(@RequestParam String personId,@RequestParam String fromDate,@RequestParam String toDate)
    {
        LOGGER.info("Creation of availability period for person (`{}`) from (`{}`) to (`{}`) requested",personId,fromDate,toDate); //TODO add authentication info here, aka who accessed this


        Integer parsedPersonId=null;
        try {
            parsedPersonId=Integer.parseInt(personId);
        } catch (NumberFormatException e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) since person id is not a valid integer",personId,fromDate,toDate);
            throw new InvalidParameterException("Provided value ("+personId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) due to unknown error related to person id",personId,fromDate,toDate);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the person id parameter");
        }

        //Link for java.sql.date info https://docs.oracle.com/javase/8/docs/api/java/sql/Date.html#valueOf-java.lang.String- 

        Date parsedFromDate=null;
        try {
            parsedFromDate=Date.valueOf(fromDate);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) since from date is not a date",personId,fromDate,toDate);
            throw new InvalidParameterException("Provided value ("+fromDate+") could not be parsed as a valid date, please use the yyyy-(m)m-(d)d format, with the (m) and (d) specifying that these can be 0 or ignored" );
        }
        catch(Exception e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) due to unknown error related to from date",personId,fromDate,toDate);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the from date parameter");
        }

        Date parsedToDate=null;
        try {
            parsedToDate=Date.valueOf(toDate);
        } catch (IllegalArgumentException e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) since to date is not a date",personId,fromDate,toDate);
            throw new InvalidParameterException("Provided value ("+toDate+") could not be parsed as a valid date, please use the yyyy-(m)m-(d)d format, with the (m) and (d) specifying that these can be 0 or ignored" );
        }
        catch(Exception e) {
            LOGGER.error("Failed to create availability for person (`{}`) from (`{}`) to (`{}`) due to unknown error related to to date",personId,fromDate,toDate);
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the to date parameter");
        }

        return applicationService.CreateAvailability(parsedPersonId, parsedFromDate, parsedToDate);
    }

    /**
     * This function 
     * @param requestBody
     * To give an example of the request body, the following is a basic example of how it could look:
     * <p>{
     *   "personId":1014,
     *   "availabilityIds":[20872],
     *   "competenceProfileIds":[6488]
     *   }<p>
     * @return If it does not throw an exception, the newly created application is returned
     */
    @PostMapping("/submitApplication")
    public ApplicationDTO SubmitApplication(@RequestBody ApplicationSubmissionRequestBody requestBody)
    {
        LOGGER.info("Creation of application for person (`{}`) requested",requestBody.getPersonId()); //TODO add authentication info here, aka who accessed this
        return applicationService.SubmitApplication(requestBody.getPersonId(),requestBody.getAvailabilityIds(),requestBody.getCompetenceProfileIds());
    }
}
