package com.example.demo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.ApplicantReset;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.InvalidJWTException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.InvalidPersonException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.repository.ApplicantResetRepository;
import com.example.demo.repository.PersonRepository;
import com.example.demo.repository.RoleRepository;

import jakarta.transaction.RollbackException;
import jakarta.validation.ConstraintViolationException;


@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
/**
 * This service class is for handling person-related operations such as registering,
 * updating, and resetting information, as well as generating and validating JWT tokens.
 * It uses explicit transaction annotation to ensure a rollback occurs whenever an unchecked exception is thrown.
 */
public class PersonService implements UserDetailsService {
    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ApplicantResetRepository applicantResetRepository;
    // We create the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class.getName());

    /**
     * Constructs a new instance of the PersonService (Spring boot managed).
     * 
     * @param personRepository the repository for accessing person database data
     * @param roleRepository   the repository for accessing role database information
     * @param applicantResetRepository the repository for handling applicant reset requests
     * @param jwtService       this service provides jwt token related functionality
     * @param passwordEncoder  this is responsible for encoding passwords
     */
    public PersonService(PersonRepository personRepository, RoleRepository roleRepository,
            ApplicantResetRepository applicantResetRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.applicantResetRepository = applicantResetRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This function returns a list of people whose names match the provided string
     * 
     * @param name The name to find a match for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @return A list of matching people
     */
    public List<? extends PersonDTO> FindPeopleByName(String name) throws CustomDatabaseException {
        try {
            return personRepository.findByName(name);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to find people by name (`{}`) due to a database error : (`{}`)", name, e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * This function adds a new person, with the specified fields
     * 
     * @param name     The name of the person to add
     * @param surname  The surname of the person to add
     * @param pnr      The pnr of the person to add
     * @param email    The email of the person to add
     * @param password The password of the person to add
     * @param username The username of the person to add
     * @throws IllegalArgumentException this exception is thrown if the parameters already exist
     * @throws CustomDatabaseException  this is thrown if any of the jpa methods
     */
    public void RegisterPerson(String name, String surname, String pnr, String email, String password, String username)
      throws IllegalArgumentException, CustomDatabaseException {

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

            LOGGER.info("Registering new person: Name: `{}`, Surname: `{}`, PNR: `{}`, Email: `{}`, Username: `{}`",
                    name, surname, pnr, email, username);
            personRepository.save(person);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to find people by name (`{}`) due to a database error : (`{}`)", name, e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their email
     * 
     * @param email The email to search for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @return The Person entity found, or empty if not found
     */
    public Optional<? extends PersonDTO> FindPersonByEmail(String email) throws CustomDatabaseException {
        try {
            return personRepository.findByEmail(email);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to find people by email (`{}`) due to a database error : (`{}`)", email,
                    e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their username
     * 
     * @param username The username to search for
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @return The Person entity found, or empty if not found
     */
    public Optional<? extends PersonDTO> FindPersonByUsername(String username) throws CustomDatabaseException {
        try {
            return personRepository.findByUsername(username);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to find people by username (`{}`) due to a database error : (`{}`)", username,
                    e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Finds a person by their personal identity number
     * 
     * @param pnr The personal identity number to search for.
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @return TThe Person entity found, or empty if not found
     */
    public Optional<? extends PersonDTO> FindPersonByPnr(String pnr) throws CustomDatabaseException {
        try {
            return personRepository.findByPnr(pnr);
        } catch (DataAccessException e) {
            LOGGER.error("Failed to find people by pnr (`{}`) due to a database error : (`{}`)", pnr, e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * This method is used to update pnr and email for a Recruiter
     * 
     * @param personId The person id for the reviwer to update
     * @param pnr      The new pnr
     * @param email    The new email
     * @throws PersonNotFoundException if the personId does not match a existing person
     * @throws InvalidPersonException  if the person is not a reviewer
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @throws TransactionSystemException if something causes a transaction exception
     * @return If successfull, a string describing this
     */
    public String UpdateRecruiter(Integer personId, String pnr, String email) throws PersonNotFoundException, InvalidPersonException, CustomDatabaseException,TransactionSystemException {
        
        try {
            Optional<Person> personContainer = personRepository.findById(personId);

            if (personContainer.isEmpty()) {
                LOGGER.error(
                        "Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) since no person exists in the database with that id",
                        personId, pnr, email);
                throw new PersonNotFoundException(personId);
            }

            Person person = personContainer.get();

            // We confirm the user is a recruiter, if not this endpoint should not be used
            if (!person.getRole().getName().equals("recruiter")) {
                LOGGER.error(
                        "Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) since person is not a recruiter, they are a (`{}`)",
                        personId, pnr, email, person.getRole().getName());
                throw new InvalidPersonException("You are not a recruiter, so this endpoint is not for you!");
            }

            person.setPnr(pnr);
            person.setEmail(email);
            personRepository.save(person);

            LOGGER.info("Updated pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`)", personId, pnr,
                    email);

            return "Updated pnr and email for a reviwer " + person.getName() + " to pnr " + pnr + " and email " + email;

        } catch (DataAccessException e) {
            LOGGER.error(
                    "Failed to update pnr and email for a reviwer (`{}`) to pnr (`{}`) and email (`{}`) due to a database error : (`{}`)",
                    personId, pnr, email, e.getMessage());
            throw new CustomDatabaseException();
        }     
        
    }

    /**
     * This method is used to request an update of username and password for a
     * applicant, which is done by generating a unique link to the users email
     * 
     * @param email The email for the applicant to generate a reset link for
     * @throws InvalidPersonException  if the email does not match a existing person or that person is not an applicant
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail
     * @return a message indicating the reset link was sent
     */
    public String ApplicantResetLinkGeneration(String email) throws InvalidPersonException, CustomDatabaseException {
        try {
            Optional<Person> personContainer = personRepository.findByEmail(email);

            if (personContainer.isEmpty()) {
                LOGGER.error(
                        "Failed to generate reset link for a applicant with email (`{}`) since no person with that pnr exists in the database",
                        email);
                throw new InvalidPersonException("No person with that email exists!");
            }

            Person person = personContainer.get();

            // We confirm the user is a applicant, if not this endpoint should not be used
            if (!person.getRole().getName().equals("applicant")) {
                LOGGER.error(
                        "Failed to generate reset link for a applicant with email (`{}`) since person is not a applicant, they are a (`{}`)",
                        email, person.getRole().getName());
                throw new InvalidPersonException("You are not a applicant, so this endpoint is not for you!");
            }

            // We then generate the unique link, by creating a jwt token with a unique
            // random number + timestamp, then

            String uniqueToken = jwtService.generateResetToken(email);
            Date expirationDate = jwtService.extractExpiration(uniqueToken);
            Long randomNumber = jwtService.extractRandomNumber(uniqueToken);

            ApplicantReset reset = new ApplicantReset();
            reset.setPerson(person);
            reset.setResetDate(expirationDate.toString());
            reset.setRandomLong(randomNumber);

            // Here the link is sent using "email"
            LOGGER.info("Unique token generated is : " + uniqueToken);

            applicantResetRepository.save(reset);

            return "Reset link sent to email " + person.getEmail();
        } catch (DataAccessException e) {
            LOGGER.error(
                    "Failed to generate reset link for a applicant with email (`{}`) due to a database error : (`{}`)",
                    email, e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    /**
     * Uses a reset token to update an applicant's username and password.
     *
     * @param resetToken the reset token received by the applicant
     * @param username   the new username
     * @param password   the new password
     * @throws InvalidJWTException if the reset token is invalid or expired
     * @throws CustomDatabaseException if a database error occurs during the update
     * @throws TransactionSystemException if something causes a transaction exception
     * @return A message confirming the update and showing the new username.
     */
    public String ApplicantUseResetLink(String resetToken, String username, String password)
      throws InvalidJWTException, CustomDatabaseException,TransactionSystemException {
        
        try {

            if (!jwtService.validateResetToken(resetToken)) {
                LOGGER.error(
                        "Failed to reset applicant following link with token (`{}`) since token is either invalid or out of date",
                        resetToken);
                throw new InvalidJWTException("Token invalid, either out of date or not generated by this system");
            }

            String email = jwtService.extractSubject(resetToken);

            Optional<Person> personContainer = personRepository.findByEmail(email);

            if (personContainer.isEmpty()) {
                LOGGER.error(
                        "Failed to update applicant following link with token (`{}`) which contains email (`{}`) since no person with that pnr exists in the database",
                        resetToken, email);
                throw new InvalidPersonException("No person with that email exists!");
            }

            Person person = personContainer.get();

            // We confirm the user is a applicant, if not this endpoint should not be used
            if (!person.getRole().getName().equals("applicant")) {
                LOGGER.error(
                        "Failed to update applicant following link with token (`{}`) which contains email (`{}`) since person is not a applicant, they are a (`{}`)",
                        resetToken, email, person.getRole().getName());
                throw new InvalidPersonException("You are not a applicant, so this endpoint is not for you!");
            }

            Optional<ApplicantReset> resetContainer = applicantResetRepository.findByPersonAndResetDateAndRandomLong(
                    person, jwtService.extractExpiration(resetToken).toString(),
                    jwtService.extractRandomNumber(resetToken));

            if (resetContainer.isEmpty()) {
                LOGGER.error(
                        "Failed to update applicant following link with token (`{}`) which contains email (`{}`) since no reset request for that person was found",
                        resetToken, email);
                throw new InvalidJWTException(
                        "You gave an invalid but potentially real token, no current request for that person exists in the system. The link may have already been used, in which case you must request a new one.");
            }

            // We then update the username and password for the user
            person.setUsername(username);
            person.setPassword(passwordEncoder.encode(password));
            personRepository.save(person);

            // We then remove the token, since we want to avoid a token being re-used in
            // this case
            applicantResetRepository.delete(resetContainer.get());

            return "User updated, it now has the username " + person.getUsername();
        } catch (DataAccessException e) {
            LOGGER.error("Failed to update applicant following link with token (`{}`) due to a database error : (`{}`)",
                    resetToken, e.getMessage());
            throw new CustomDatabaseException();
        }
    }

    @Override
    /**
     * This method accepts a username, and attepts to find a matching user in the database,
     * and if such exists it return a UserDetails for that user
     * 
     * @param username the username to find a user using
     * @throws CustomDatabaseException if a database error occurs
     * @throws UsernameNotFoundException if the username was not found
     * @return If no exception is thrown, a user details for the user with the specified username
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            Optional<Person> personDetail = personRepository.findByUsername(username);
            return personDetail.map(PersonDetails::new).orElseThrow(() -> new UsernameNotFoundException("Could not find username: " + username));

        } catch (DataAccessException e) {
            LOGGER.error("Failed to load a user by username (`{}`) due to a database error : (`{}`)", username, e.getMessage());
            throw new CustomDatabaseException();
        }
    }
}
