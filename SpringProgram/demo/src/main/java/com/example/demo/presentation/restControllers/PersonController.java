package com.example.demo.presentation.restControllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.requestBodies.PersonRegistrationRequestBody;
import com.example.demo.service.PersonService;

@RestController
@RequestMapping("/person")
@CrossOrigin //This uses the config in config/WebConfig.java to allow cross-origin access
/**
 * This endpoint controller is responsible for handeling the requests concerning people application.
 * This includes, for example, finding the people with a specific name
 */
public class PersonController {
    private final PersonService personService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class.getName()); 


    @Autowired
    /**
     * Constructs a new instance of the PersonController (this is Spring boot managed).
     * @param personService The service used to handle people related manners.
     */
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    /**
     * This function creates a new person with the specified parameters, and then returns a string with the new person´s information
     * @param name the first name of the new person
     * @param surname the last name of the new person
     * @return a string confirming the new person
     */
    @PostMapping("/add")
    public String addPerson(@RequestParam String name, @RequestParam String surname){
        LOGGER.info("Creation of new user with name (`{}`) and surname (`{}`) applications requested",name,surname); //TODO add authentication info here, aka who accessed this. Also add any new fields to log entry here
        personService.AddPerson(name, surname);
        return "Person added: " + name + " " + surname;
    }

    /**
     * This function registers a new person using the provided request body.
     * @param requestBody The request body containing the person's information.
     * @return A response indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerPerson(@RequestBody PersonRegistrationRequestBody requestBody) {
        try {
            LOGGER.info("Registration requested for user with Name: `{}`, Surname: `{}`, PNR: `{}`, Email: `{}`, Username `{}`", 
                        requestBody.getName(), requestBody.getSurname(), requestBody.getPnr(), requestBody.getEmail(), requestBody.getUsername());

            personService.RegisterPerson(
                requestBody.getName(),
                requestBody.getSurname(),
                requestBody.getPnr(),
                requestBody.getEmail(),
                requestBody.getPassword(),
                requestBody.getUsername()
            );

            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This function returns a list of people whose name match the specified name
     * @param name the name to find user's whose name match with
     * @return a list of people with names matching with the name parameter
     */
    @GetMapping("/find")
    public List<? extends PersonDTO> findPersonByName(@RequestParam String name){
        LOGGER.info("People with the name (`{}`) requested", name); //TODO add authentication info here, aka who accessed this
        return personService.FindPeopleByName(name);
    }
}
