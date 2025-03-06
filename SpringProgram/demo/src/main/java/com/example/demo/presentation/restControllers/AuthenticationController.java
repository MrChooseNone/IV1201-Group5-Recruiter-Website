package com.example.demo.presentation.restControllers;

import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.service.JwtService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class.getName());

    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticationController(JwtService jwtService, AuthenticationManager authenticationManager){
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestParam String username, @RequestParam String password){

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
            return "{\"token\": \""+jwtService.generateToken(username)+"\" , \"role\":\""+authentication.getAuthorities().iterator().next().toString()+"\"}";
        }
        else{
            throw new UsernameNotFoundException("INVALID USER REQUEST");
        }
    }

}
