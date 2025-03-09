package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.repository.PersonRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
/**
 * Service responsible for handling authentication-related operations.
 * This service interacts with the PersonRepository to retrieve user data
 * and manages password encoding using BCrypt.
 */
public class AuthenticationService {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructs an instance of AuthenticationService.
     *
     * @param personRepository  The repository for accessing person-related data.
     * @param passwordEncoder   The password encoder used for hashing passwords.
     */
    public AuthenticationService(PersonRepository personRepository, BCryptPasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
