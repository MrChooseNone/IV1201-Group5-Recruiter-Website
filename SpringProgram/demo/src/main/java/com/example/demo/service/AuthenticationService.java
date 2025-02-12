package com.example.demo.service;

import org.springframework.stereotype.Service;
import com.example.demo.repository.PersonRepository;
import com.example.demo.domain.entity.Person;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.lang.RuntimeException;

@Service
public class AuthenticationService {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthenticationService(PersonRepository personRepository, BCryptPasswordEncoder passwordEncoder){
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


}
