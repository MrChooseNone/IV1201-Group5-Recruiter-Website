package com.example.demo.presentation.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.demo.domain.PersonDetails;
import com.example.demo.domain.entity.Person;
import com.example.demo.domain.entity.Role;
import com.example.demo.presentation.restControllers.AuthenticationController;
import com.example.demo.service.JwtService;

@ExtendWith(MockitoExtension.class)
/**
 * This tests the AuthenticationEndpointControllerTest class
 */
public class AuthenticationEndpointControllerUnitTest {
    
    @Spy
    private JwtService jwtService;

    @Spy
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationController authenticationEndpointController;

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(AuthenticationEndpointControllerUnitTest.class);
    }

    @Test
    /**
     * This tests the generateToken method
     */
    void generateTokenTest()
    {
        //We first create the fake data

        //We then define the mock function implementations

        Role role = new Role();
        role.setName("testRole");
        Person person = new Person();
        person.setRole(role);
        person.setId(0);
        PersonDetails details=new PersonDetails(person);
        Authentication authentication=new UsernamePasswordAuthenticationToken(details,null,details.getAuthorities());

        // We then define the mock implementation for the service function
        when(authenticationManager.authenticate(any(Authentication.class))).thenAnswer(invocation -> {
            return authentication;
        });
    
        //We first test that a "correct" requests works
        authenticationEndpointController.authenticateAndGetToken("bla","bla");

        //We then test that if authentication is false that it will throw the correct exception
        authentication.setAuthenticated(false);
        assertThrowsExactly(UsernameNotFoundException.class, () -> authenticationEndpointController.authenticateAndGetToken("bla","bla"));

        //This tests that the authentication exception is handleded correctly
        doThrow(new AuthenticationException("Oops! Something went wrong.") {}).when(authenticationManager).authenticate(any(Authentication.class));
        
        //This is a weird way of testing if it throws the AuthenticationException it recives from authenticationManager
        try {
            authenticationEndpointController.authenticateAndGetToken("bla","bla");
            assertFalse(true);
        } catch (AuthenticationException e) {
            assertEquals("Oops! Something went wrong.", e.getMessage());
        }

    }

}
