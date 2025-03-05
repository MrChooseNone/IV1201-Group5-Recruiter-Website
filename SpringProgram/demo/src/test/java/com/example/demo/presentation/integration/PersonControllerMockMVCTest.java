package com.example.demo.presentation.integration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restControllers.PersonController;
import com.example.demo.presentation.unit.PersonControllerUnitTest;
import com.example.demo.service.PersonService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
// Based on the guide https://spring.io/guides/gs/testing-web
// This is used to specify the property file :
// https://docs.spring.io/spring-framework/reference/testing/annotations/integration-spring/annotation-testpropertysource.html
@ActiveProfiles("test") // This is done for the purpose of loading the application-test properties file
@SpringBootTest(properties = { "spring.profiles.active=test" })
/**
 * This class performs mock mvc testing of PersonController
 */
public class PersonControllerMockMVCTest {

    // This is used to test endpoint controllers without running the full server
    @Autowired
    private MockMvc mockMvc;

    /**
     * This tests that a request without a parameter will return a response with bad
     * request http code with the correct error message
     * 
     * @throws Exception
     */
    @WithMockUser(authorities = "recruiter") //This mocks the authentication already having been done
    @Test
    void findPersonNoParameterEndpointTest() throws Exception {
        this.mockMvc.perform(get("/person/findPerson")).andDo(print()).andExpect(status().isBadRequest())
                .andExpect(content().string("Please provide PNR, email, or username for search."));
    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "recruiter") //This mocks the authentication already having been done
    void findPersonPnrEndpointTest() throws Exception {
        this.mockMvc.perform(get("/person/findPerson?pnr=20070114-1252")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"id\":11,\"name\":\"Leroy\",\"surname\":\"Crane\",\"pnr\":\"20070114-1252\",\"email\":\"l_crane118@finnsinte.se\",\"role\":{\"roleId\":2,\"name\":\"applicant\"},\"username\":\"badUsername\"}"));
    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "recruiter") //This mocks the authentication already having been done
    void findPersonEmailEndpointTest() throws Exception {
        // We first set up the mock implementation of the function
        this.mockMvc.perform(get("/person/findPerson?email=l_crane118@finnsinte.se")).andDo(print())
                .andExpect(status().isOk()).andExpect(content().string("{\"id\":11,\"name\":\"Leroy\",\"surname\":\"Crane\",\"pnr\":\"20070114-1252\",\"email\":\"l_crane118@finnsinte.se\",\"role\":{\"roleId\":2,\"name\":\"applicant\"},\"username\":\"badUsername\"}"));
    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test    
    @WithMockUser(authorities = "recruiter") //This mocks the authentication already having been done
    void findPersonUsernameEndpointTest() throws Exception {

        this.mockMvc.perform(get("/person/findPerson?username=JoelleWilkinson")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Joelle\",\"surname\":\"Wilkinson\",\"pnr\":\"testsson\",\"email\":\"froghead\",\"role\":{\"roleId\":1,\"name\":\"recruiter\"},\"username\":\"JoelleWilkinson\"}"));

    }

    /**
     * This tests that a request which will return a response with 404
     * http status code with the correct error message
     * @throws Exception
     */
    @Test
    @WithMockUser(authorities = "recruiter") //This mocks the authentication already having been done
    void findPersonUsernameMissingUsernameEndpointTest() throws Exception {

        this.mockMvc.perform(get("/person/findPerson?username=notARealPerson")).andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Person not found."));

    }

}
