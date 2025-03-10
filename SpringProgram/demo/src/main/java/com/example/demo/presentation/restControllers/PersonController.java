package com.example.demo.presentation.restControllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.requestBodies.PersonRegistrationRequestBody;
import com.example.demo.service.PersonService;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/person")
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}") // This uses the config in config/WebConfig.java to allow cross-origin access
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
     * @param pnr      The new pnr
     * @param email    The new email
     * @return A string describing the success status
     */
    @PostMapping("/updateRecruiter")
    @PreAuthorize("hasAuthority('recruiter')")
    public String UpdateRecruiter(@RequestParam String pnr, @RequestParam String email) {
        PersonDetails userAuthentication=((PersonDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()); //This retrives the currently authenticated users person details
        LOGGER.info("Update of pnr and email for reviwer (`{}`), pnr (`{}`) and email (`{}`) by (`{}`)",userAuthentication.getPersonId(),pnr,email, userAuthentication.getUsername());
        return personService.UpdateRecruiter(userAuthentication.getPersonId(),pnr,email);
    }

    /**
     * This method allows an existing applicant to request a reset link for their account be sent to their email
     * 
     * @param personId The pnr of the person to reset
     * @return A string describing the success status
     *  TODO is this a good level of security, or what else could be used?
     */
    @PostMapping("/requestApplicantReset")
    @PreAuthorize("hasAuthority('applicant')")
    public String RequestApplicantReset(@RequestParam String email) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.info("Update of username and password for applicant with email (`{}`) requested" + email + "by user", currentUser);
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
    @PreAuthorize("hasAuthority('applicant')")
    public String UpdateApplicant(@RequestParam String resetToken, @RequestParam String username, @RequestParam String password) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.info("Update of username and password for applicant with resetToken (`{}`) to username (`{}`) requested" + resetToken,username + "request made by user", currentUser);
        return personService.ApplicantUseResetLink(resetToken,username,password);
    }

    /**
     * This function returns a list of people whose name match the specified name
     * 
     * @param name the name to find user's whose name match with
     * @return a list of people with names matching with the name parameter
     */
    @GetMapping("/find")
    @PreAuthorize("hasAuthority('recruiter')")
    public List<? extends PersonDTO> findPersonByName(@RequestParam String name) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        LOGGER.info("People with the name (`{}`) requested" + name + "request made by user:", currentUser); 
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
    @PreAuthorize("hasAuthority('recruiter')")
    public ResponseEntity<?> findPerson(Authentication authentication,
            @RequestParam(required = false) String pnr,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username) {

        LOGGER.info(authentication.getPrincipal().getClass().getName());

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
