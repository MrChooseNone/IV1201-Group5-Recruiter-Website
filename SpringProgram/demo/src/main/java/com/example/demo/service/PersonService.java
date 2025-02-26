package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.InvalidPersonException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RoleRepository;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) 
public class PersonService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;

    //We create the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class.getName()); 

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of the PersonService (Spring boot managed).
     *
     * @param personRepository the repository for accessing person database data
     */
    public PersonService(PersonRepository personRepository,RoleRepository roleRepository) {
        this.personRepository = personRepository;
        this.roleRepository=roleRepository;
    }

    /**
     * This function returns a list of people whose names match the provided string
     * @param name The name to find a match for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return A list of matching people
     */
    public List<? extends PersonDTO> FindPeopleByName(String name){
        try {
            return personRepository.findByName(name);
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to find people by name (`{}`) due to a database error : (`{}`)",name,e.getMessage());
            throw new CustomDatabaseException();
        }
    }


    /**
     * This function adds a new person, with the specified fields
     * @param name The name of the person to add
     * @param surname The surname of the person to add
     * @param pnr The pnr of the person to add
     * @param email The email of the person to add
     * @param password The password of the person to add
     * @param username The username of the person to add
     * @throws IllegalArgumentException this exception is thrown if the parameters already exist
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     */
    public void RegisterPerson(String name, String surname, String pnr, String email, String password, String username) {
        
        try {
            if (personRepository.existsByPnr(pnr)) {
                LOGGER.warn("Attempt to register with duplicate PNR: {}", pnr);
                throw new IllegalArgumentException("PNR is already in use!");
            }
            if (personRepository.existsByEmail(email)) {
                LOGGER.warn("Attempt to register with duplicate email: {}", email);
                throw new IllegalArgumentException("Email is already registered!");
            }
            if (personRepository.existsByUsername(username)) {
                LOGGER.warn("Attempt to register with duplicate username: {}", username);
                throw new IllegalArgumentException("Username is already taken!");
            }

            Person person = new Person();
            person.setName(name);
            person.setSurname(surname);
            person.setPnr(pnr);
            person.setEmail(email);
            person.setPassword(passwordEncoder.encode(password));
            person.setUsername(username);
            person.setRole(roleRepository.findByName("applicant"));

            LOGGER.info("Registering new person: Name: `{}`, Surname: `{}`, PNR: `{}`, Email: `{}`, Username: `{}`", name, surname, pnr, email, username);
            personRepository.save(person);
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to find people by name (`{}`) due to a database error : (`{}`)",name,e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their email
     * @param email The email to search for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByEmail(String email) {
        try {
            return personRepository.findByEmail(email);
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to find people by email (`{}`) due to a database error : (`{}`)",email,e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their username
     * @param username The username to search for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByUsername(String username) {
        try {
            return personRepository.findByUsername(username);
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to find people by username (`{}`) due to a database error : (`{}`)",username,e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their personal identity number
     * @param pnr The personal identity number to search for.
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByPnr(String pnr) {
        try {
            return personRepository.findByPnr(pnr);
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to find people by pnr (`{}`) due to a database error : (`{}`)",pnr,e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * This method is used to update pnr and email for a reviewer
     * @param personId The person id for the reviwer to update
     * @param pnr The new pnr
     * @param email The new email
     * @throws PersonNotFoundException if the personId does not match a existing person
     * @throws InvalidPersonException if the person is not a reviewer
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return If successfull, a string describing this
     */
    public String UpdateReviewer(Integer personId, String pnr, String email)
    {
        try {
            Optional<Person> personContainer = personRepository.findById(personId);
            
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) since no person exists in the database with that id",personId,pnr,email);
                throw new PersonNotFoundException(personId);
            }

            Person person=personContainer.get();

            //We confirm the user is a recruiter, if not this endpoint should not be used
            if (!person.getRole().getName().equals("recruiter")) {
                LOGGER.error("Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) since person is not a recruiter, they are a (`{}`)",personId,pnr,email,person.getRole().getName());
                throw new InvalidPersonException("You are not a recruiter, so this endpoint is not for you!");
            }

            person.setPnr(pnr);
            person.setEmail(email);
            personRepository.save(person);

            LOGGER.info("Updated pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`)", personId,pnr,email);

            return "Updated pnr and email for a reviwer "+person.getName()+" to pnr "+pnr+" and email " + email;

        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) due to a database error : (`{}`)",personId,pnr,email,e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * This method is used to update username and password for a applicant
     * @param pnr The pnr for the applicant to update
     * @param pnr 
     * @param email
     * @throws InvalidPersonException if the pnr does not match a existing person or that person is not an applicant
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @return If no exception is thrown, a string describing the success
     */
    public String UpdateApplicant(String pnr, String username, String password)
    {
        try {
            Optional<Person> personContainer = personRepository.findByPnr(pnr);
            
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to update username and password for a applicant (`{}`) to username (`{}`) since no person exists in the database with that id",pnr,username);
                throw new InvalidPersonException("No person with that pnr exists!");
            }

            Person person=personContainer.get();

            //We confirm the user is a recruiter, if not this endpoint should not be used
            if (!person.getRole().getName().equals("applicant")) {
                LOGGER.error("Failed to update username and password for a applicant (`{}`) to username (`{}`) since person is not a applicant, they are a (`{}`)",pnr,username,person.getRole().getName());
                throw new InvalidPersonException("You are not a applicant, so this endpoint is not for you!");
            }

            person.setUsername(username);
            person.setPassword(passwordEncoder.encode(password)); 
            personRepository.save(person);

            LOGGER.info("Updated username and password for a applicant (`{}`) to username",pnr,username);

            return "Updated username and password for a applicant "+person.getName()+" to username "+person.getUsername()+" (password excludes :) )";
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to update username and password for an applicant (`{}`) to username (`{}`) due to a database error : (`{}`)",pnr,username,e.getMessage());
            throw new CustomDatabaseException();
        }
    }
}
