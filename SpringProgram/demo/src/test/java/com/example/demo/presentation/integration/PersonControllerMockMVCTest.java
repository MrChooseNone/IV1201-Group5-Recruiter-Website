package com.example.demo.presentation.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.requestBodies.PersonRegistrationRequestBody;
import com.example.demo.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private JwtService jwtService;

    // This is used to test endpoint controllers without running the full server
    @Autowired
    private MockMvc mockMvc;

    //This is used to transform the request body classes to string
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * This tests that a request without a parameter will return a response with bad
     * request http code with the correct error message. Note that it uses jwtService to create a fake but valid token.
     * 
     * @throws Exception This throws any exception which occurs
     */
    @Test
    void findPersonNoParameterEndpointTest() throws Exception {
        this.mockMvc.perform(get("/person/findPerson").header("Authorization", "Bearer "+jwtService.generateToken("JoelleWilkinson"))).andDo(print()).andExpect(content().string("Please provide PNR, email, or username for search.")).andExpect(status().isBadRequest());
    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test
    void findPersonPnrEndpointTest() throws Exception {

        this.mockMvc.perform(get("/person/findPerson?pnr=20070114-1252").header("Authorization", "Bearer "+jwtService.generateToken("JoelleWilkinson"))).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string("{\"id\":11,\"name\":\"Leroy\",\"surname\":\"Crane\",\"pnr\":\"20070114-1252\",\"email\":\"l_crane118@finnsinte.se\",\"role\":{\"roleId\":2,\"name\":\"applicant\"},\"username\":null}"));

    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test
    void findPersonEmailEndpointTest() throws Exception {

        this.mockMvc.perform(get("/person/findPerson?email=l_crane118@finnsinte.se").header("Authorization", "Bearer "+jwtService.generateToken("JoelleWilkinson"))).andDo(print())
                .andExpect(status().isOk()).andExpect(content().string("{\"id\":11,\"name\":\"Leroy\",\"surname\":\"Crane\",\"pnr\":\"20070114-1252\",\"email\":\"l_crane118@finnsinte.se\",\"role\":{\"roleId\":2,\"name\":\"applicant\"},\"username\":null}"));

    }

    /**
     * This tests that a request which will return a response with ok
     * http status code with the correct person based on the stored values with prn
     * as a parameter (this assumes the existing database is used, if not it may be incorrect)
     * 
     * @throws Exception
     */
    @Test    
    void findPersonUsernameEndpointTest() throws Exception {
        this.mockMvc.perform(get("/person/findPerson?username=JoelleWilkinson").header("Authorization", "Bearer "+jwtService.generateToken("JoelleWilkinson"))).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("{\"id\":1,\"name\":\"Joelle\",\"surname\":\"Wilkinson\",\"pnr\":null,\"email\":null,\"role\":{\"roleId\":1,\"name\":\"recruiter\"},\"username\":\"JoelleWilkinson\"}"));

    }

    @Test
    /**
     * This tests the register person endpoint
     * Note that since this could create something new, and is a full integration test, it could make changes and depends on the real database
     * 
     */
    @Transactional
    void registerPersonEndpointTest() throws Exception
    {
        PersonRegistrationRequestBody requestBody = new PersonRegistrationRequestBody("name","surname","20070114-1252","email@emaiö.ln","password","username");
        String requestBodyString = objectMapper.writeValueAsString(requestBody);
        this.mockMvc.perform(post("/person/register").contentType(MediaType.APPLICATION_JSON).content(requestBodyString)).andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("PNR is already in use!"));
        
        PersonRegistrationRequestBody requestBody2 = new PersonRegistrationRequestBody("name","surname","wadsdawdasd","l_crane118@finnsinte.se","password","username");
        String requestBodyString2 = objectMapper.writeValueAsString(requestBody2);
        this.mockMvc.perform(post("/person/register").contentType(MediaType.APPLICATION_JSON).content(requestBodyString2)).andDo(print())
                        .andExpect(status().isBadRequest())
                        .andExpect(content().string("Email is already registered!"));
       
        PersonRegistrationRequestBody requestBody3 = new PersonRegistrationRequestBody("name","surname","wadsdawdasd","email@emaiasdö.ln","password","MartinCummings");
        String requestBodyString3 = objectMapper.writeValueAsString(requestBody3);
        this.mockMvc.perform(post("/person/register").contentType(MediaType.APPLICATION_JSON).content(requestBodyString3)).andDo(print())
                                        .andExpect(status().isBadRequest())
                                        .andExpect(content().string("Username is already taken!"));

    }

}
