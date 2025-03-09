package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;

@ExtendWith(MockitoExtension.class)
/**
 * This class defined the unit tests for the JwtService class
 * Note that it focuses on how the methods interact, and that these are equivelent to the correct jwt token methods 
 */
public class JwtServiceTest {
    
    @Spy
    private JwtService jwtService;

    @Test
    /**
     * This tests the generateToken method
     */
    void generateTokenTest()
    {
        Role role = new Role();
        role.setName("testRole");
        Person person = new Person();
        person.setRole(role);
        person.setId(0);
        person.setUsername("anotherUsername");
        PersonDetails details=new PersonDetails(person);
        String token = jwtService.generateToken("test");
        assertEquals("test", jwtService.extractSubject(token)); //We test the username can be extracted
        assertFalse(jwtService.validateToken(token,details )); //We confirm that it will not match with a PersonDetails for a user with another username
        person.setUsername("test");
        assertTrue(jwtService.validateToken(token,details )); //We confirm that it will match with a PersonDetails for a user with the same username

    }


    @Test
    /**
     * This tests the generateResetToken method
     */
    void generateResetTokenTest()
    {
        String resetToken=jwtService.generateResetToken("test");
        assertEquals("test", jwtService.extractSubject(resetToken)); //This tests that the subject can be extracted
        assertEquals(Long.class, jwtService.extractRandomNumber(resetToken).getClass()); //This tests that the random number is a long 
        assertTrue(jwtService.validateResetToken(resetToken)); //This tests that the token generated is valid
    }
}
