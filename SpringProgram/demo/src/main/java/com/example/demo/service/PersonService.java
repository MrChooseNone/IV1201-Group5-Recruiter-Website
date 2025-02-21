package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Person;
import com.example.demo.repository.PersonRepository;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) 
public class PersonService {
    private final PersonRepository personRepository;

    //We create the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class.getName()); 

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new instance of the PersonService (Spring boot managed).
     *
     * @param personRepository the repository for accessing person database data
     */
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    /**
     * This function returns a list of people whose names match the provided string
     * @param name The name to find a match for
     * @return A list of matching people
     */
    public List<? extends PersonDTO> FindPeopleByName(String name){
        return personRepository.findByName(name);
    }

    /**
     * This function adds a new person, with the specified fields
     * @param name the name of the person to add
     * @param surname the surname of the person to add
     */
    public void AddPerson(String name,String surname, String password)
    {
        Person person = new Person();
        person.setName(name);
        person.setSurname(surname);
        person.setPassword(passwordEncoder.encode(password));
        LOGGER.info("Added new person with name (`{}`) and surname (`{}`)",name,surname); //TODO add more parameters here when those are added
        personRepository.save(person);        
    }

    /**
     * This function adds a new person, with the specified fields
     * @param name The name of the person to add
     * @param surname The surname of the person to add
     * @param pnr The pnr of the person to add
     * @param email The email of the person to add
     * @param password The password of the person to add
     * @param username The username of the person to add
     */
    public void RegisterPerson(String name, String surname, String pnr, String email, String password, String username) {
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
        person.setPassword(password);
        person.setUsername(username);

        LOGGER.info("Registering new person: Name: `{}`, Surname: `{}`, PNR: `{}`, Email: `{}`, Username: `{}`", name, surname, pnr, email, username);
        personRepository.save(person);
    }

    /**
     * Finds a person by their email
     * @param email The email to search for
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    /**
     * Finds a person by their username
     * @param username The username to search for
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByUsername(String username) {
        return personRepository.findByUsername(username);
    }

    /**
     * Finds a person by their personal identity number
     * @param pnr The personal identity number to search for.
     * @return The found Person entity, if present
     */
    public Optional<? extends PersonDTO> FindPersonByPnr(String pnr) {
        return personRepository.findByPnr(pnr);
    }
}
