package com.example.demo.presentation.restControllers;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.service.JwtService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.demo.service.PersonService;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "${ALLOWED_ORIGINS:http://localhost:3000}") // This uses the config in config/WebConfig.java to allow cross-origin access
/**
 * This endpoint ontroller is responsible for handling authentication-related requests.
 * Provides an endpoint for user authentication and JWT token generation.
 */
public class AuthenticationController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class.getName());

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PersonService personService;

    /**
     * Constructs an instance of AuthenticationController.
     *
     * @param jwtService            The service responsible for generating JWT tokens.
     * @param authenticationManager The Spring Security authentication manager.
     */
    public AuthenticationController(JwtService jwtService, AuthenticationManager authenticationManager){
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Authenticates the user and generates a JWT token if authentication is successful.
     *
     * @param username The username of the user trying to authenticate.
     * @param password The password of the user.
     * @throws AuthenticationException If authentication fails due to invalid credentials.
     * @return A JWT token as a string if authentication is successful.
     */
    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestParam String username, @RequestParam String password)
      throws AuthenticationException{

        LOGGER.info("authenticateAndGetToken requested for user (`{}`)",username);
        Authentication authentication=null;
        try {
            authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
        } catch (AuthenticationException e) {
            LOGGER.error("authenticateAndGetToken failed due to user (`{}`) not being correctly authenticated: (`{}`)",username, e.getMessage());
            throw e;
        }

        if(authentication.isAuthenticated()){
            LOGGER.info("authenticateAndGetToken success for user (`{}`), returning token",username);
            //This returns a json in the format {"token":[tokenHere],"role":[roleHere, ex "recruiter"]}
            return "{\"token\":\""+jwtService.generateToken(username)+"\" , \"role\":\""+authentication.getAuthorities().iterator().next().toString()+"\"}";
        }
        else{
            throw new UsernameNotFoundException("INVALID USER REQUEST");
        }
    }

}
