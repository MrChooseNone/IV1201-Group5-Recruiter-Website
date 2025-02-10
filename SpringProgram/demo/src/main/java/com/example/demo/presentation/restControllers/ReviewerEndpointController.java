package com.example.demo.presentation.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.CompetenceDTO;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.ReviewService;

@RestController
@RequestMapping("/review")
/**
 * This endpoint controller is responsible for handeling the requests concerning
 * reviewing application.
 * This includes, for example, returning all application along with updating an applications status
 */
public class ReviewerEndpointController {
    @Autowired
    private final ReviewService reviewService;

    /**
     * Constructs a new instance of the ReviewerEndpointController (this is
     * Spring boot managed).
     * 
     * @param reviewService The service used to handle review related manners.
     */
    public ReviewerEndpointController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * This function returns a list of applications
     * @return This function will return the list of existing applications as a json object to the user
     */
    @GetMapping("/getApplications")
    public List<? extends ApplicationDTO> GetApplications() {
        List<? extends ApplicationDTO> applications = reviewService.GetApplications();
        return applications;
    }

    /**
     * This function returns the existing standard competences
     * 
     * @param status the application status to find applications for
     * @throws IllegalArgumentException this exceptions is thrown is a parameter is incorrectly specified
     * @return This function will return the list of existing applications with the matching status as a json object to the user
     */
    @GetMapping("/getApplicationsByStatus/{status}")
    public List<? extends ApplicationDTO> getApplicationsByStatus(@PathVariable String status) {

        ApplicationStatus parsedApplicationStatus=null;
        try {
            parsedApplicationStatus=ApplicationStatus.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Provided value ("+status+") is not valid value for application status, please specify as \"unchecked\",\"accepted\" or \"denied\" " );
        }
        catch(NullPointerException e) {
            throw new InvalidParameterException("Provided status value is null, please specify as \"unchecked\\\",\"accepted\\\" or \"denied\"  ");
        }
        catch(Exception e) {
            throw new InvalidParameterException("Unknown cause, this should never occur, but double check formating of request, specifically for status parameter");
        }

        List<? extends ApplicationDTO> applications = reviewService.GetApplicationsByStatus(parsedApplicationStatus);
        return applications;
    }

    /**
     * This function updates the application status of a specific application
     * 
     * @param status the new application status to set the application to
     * @throws IllegalArgumentException this exceptions is thrown is a parameter is incorrectly specified
     * @return The updated application as a json object
     */
    @PostMapping("/updateApplicationStatus")
    public ApplicationDTO getApplicationsByStatus(@RequestParam String applicationId,@RequestParam String status,@RequestParam String versionNumber) {
        
        ApplicationStatus parsedApplicationStatus=null;
        try {
            parsedApplicationStatus=ApplicationStatus.valueOf(status.toLowerCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidParameterException("Provided value ("+status+") is not valid value for application status, please specify as \"unchecked\",\"accepted\" or \"denied\" " );
        }
        catch(NullPointerException e) {
            throw new InvalidParameterException("Provided status value is null, please specify as \"unchecked\\\",\"accepted\\\" or \"denied\"  ");
        }
        catch(Exception e) {
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the status parameter");
        }

        Integer parsedApplicationId=null;
        try {
            parsedApplicationId=Integer.parseInt(applicationId);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Provided value ("+applicationId+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the applicationId parameter");
        }

        Integer parsedVersionNumber=null;
        try {
            parsedVersionNumber=Integer.parseInt(versionNumber);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Provided value ("+versionNumber+") could not be parsed as a valid integer" );
        }
        catch(Exception e) {
            throw new InvalidParameterException("Unknown cause, but double check formating of request, specifically for the version number parameter");
        }
        
        ApplicationDTO applications = reviewService.SetApplicationStatus(parsedApplicationId,parsedApplicationStatus,parsedVersionNumber);
        return applications;
    }

}
