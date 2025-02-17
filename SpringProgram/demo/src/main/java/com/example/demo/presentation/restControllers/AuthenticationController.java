package com.example.demo.presentation.restControllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.service.AuthenticationService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

}
