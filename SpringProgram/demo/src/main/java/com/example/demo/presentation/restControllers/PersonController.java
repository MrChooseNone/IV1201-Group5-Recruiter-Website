package com.example.demo.presentation.restControllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.requestBodies.PersonRegistrationRequestBody;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.PersonService;

@RestController
@RequestMapping("/person")
@CrossOrigin(origins = "http://localhost:3000") // This uses the config in config/WebConfig.java to allow cross-origin
                                                // access
/**
 * This endpoint controller is responsible for handeling the requests concerning
 * people application.
 * This includes, for example, finding the people with a specific name
 */
public class PersonController {
    private final PersonService personService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getName());

    @Autowired
    /**
     * Constructs a new instance of the PersonController (this is Spring boot
     * managed).
     * 
     * @param personService The service used to handle people related manners.
     */
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * This function registers a new person using the provided request body.
     * 
     * @param requestBody The request body containing the person's information.
     * @return A response indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerPerson(@RequestBody PersonRegistrationRequestBody requestBody) {
        try {
            LOGGER.info(
                    "Registration requested for user with Name: `{}`, Surname: `{}`, PNR: `{}`, Email: `{}`, Username `{}`",
                    requestBody.getName(), requestBody.getSurname(), requestBody.getPnr(), requestBody.getEmail(),
                    requestBody.getUsername());

            personService.RegisterPerson(
                    requestBody.getName(),
                    requestBody.getSurname(),
                    requestBody.getPnr(),
                    requestBody.getEmail(),
                    requestBody.getPassword(),
                    requestBody.getUsername());

            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method allows an existing reviwer to specify their pnr and email
     * 
     * @param pnr The reviwerer to update, TODO replace this with auth based id retrival if possible
     * @param pnr      The new pnr
     * @param email    The new email
     * @throws InvalidParameterException If personId is not a valid integer
     * @return A string describing the success status
     * TODO only allow a logged in reviwer to do this to their own account
     */
    @PostMapping("/updateReviwer")
    public String UpdateReviewer(@RequestParam String personId, @RequestParam String pnr, @RequestParam String email) {

        LOGGER.info("Update of pnr and email for reviwer (`{}`), pnr (`{}`) and email (`{}`) requested",personId,pnr,email); //TODO add authentication info here, aka who accessed this

        Integer parsedPersonId = null;
        try {
            parsedPersonId = Integer.parseInt(personId);
        } catch (NumberFormatException e) {
            LOGGER.error("Update of pnr and email for reviwer (`{}`), pnr (`{}`) and email (`{}`) requested",personId,pnr,email); //TODO add authentication info here, aka who accessed this
            throw new InvalidParameterException(
                    "Provided value (" + personId + ") could not be parsed as a valid integer");
        } catch (Exception e) {
            LOGGER.error("Update of pnr and email for reviwer (`{}`), pnr (`{}`) and email (`{}`) requested",personId,pnr,email); //TODO add authentication info here, aka who accessed this
            throw new InvalidParameterException(
                    "Unknown cause, but double check formating of request, specifically for the person id parameter");
        }
        return personService.UpdateReviewer(parsedPersonId,pnr,email);
    }

    /**
     * This method allows an existing applicant to request a reset link for their account be sent to their email
     * 
     * @param personId The pnr of the person to reset
     * @return A string describing the success status
     *  TODO is this a good level of security, or what else could be used?
     */
    @PostMapping("/requestApplicantReset")
    public String RequestApplicantReset(@RequestParam String email) {
        LOGGER.info("Update of username and password for applicant with email (`{}`) requested",email); //TODO add authentication info here, aka who accessed this
        return personService.ApplicantResetLinkGeneration(email);
    }


    /**
     * This method allows an existing reviwer to specify their pnr and email
     * @param personId The reviwerer to update, TODO replace this with auth based id retrival if possible
     * @param username The new username
     * @param password The new password
     * @return A string describing the success status
     *  TODO is this a good level of security, or what else could be used?
     */
    @PostMapping("/updateApplicant")
    public String UpdateApplicant(@RequestParam String resetToken, @RequestParam String username, @RequestParam String password) {

        LOGGER.info("Update of username and password for applicant with resetToken (`{}`) to username (`{}`) requested",resetToken,username); //TODO add authentication info here, aka who accessed this
        return personService.ApplicantUseResetLink(resetToken,username,password);
    }

    /**
     * This function returns a list of people whose name match the specified name
     * 
     * @param name the name to find user's whose name match with
     * @return a list of people with names matching with the name parameter
     */
    @GetMapping("/find")
    public List<? extends PersonDTO> findPersonByName(@RequestParam String name) {
        LOGGER.info("People with the name (`{}`) requested", name); // TODO add authentication info here, aka who
                                                                    // accessed this
        return personService.FindPeopleByName(name);
    }

    /**
     * Finds a person by their PNR, email, or username.
     * 
     * @param pnr      The personal identity number to search for (optional).
     * @param email    The email to search for (optional).
     * @param username The username to search for (optional).
     * @return A response containing the found person, or an error message if not
     *         found.
     */
    @GetMapping("/findPerson")
    public ResponseEntity<?> findPerson(
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {

        Optional<? extends PersonDTO> person = Optional.empty();

        if (pnr != null) {
            LOGGER.info("Searching for person with PNR `{}`", pnr);
            person = personService.FindPersonByPnr(pnr);
        } else if (email != null) {
            LOGGER.info("Searching for person with email `{}`", email);
            person = personService.FindPersonByEmail(email);
        } else if (username != null) {
            LOGGER.info("Searching for person with username `{}`", username);
            person = personService.FindPersonByUsername(username);
        } else {
            return ResponseEntity.badRequest().body("Please provide PNR, email, or username for search.");
        }

        if (person.isPresent()) {
            return ResponseEntity.ok(person.get());
        } else {
            return ResponseEntity.status(404).body("Person not found.");
        }
    }

    
}
