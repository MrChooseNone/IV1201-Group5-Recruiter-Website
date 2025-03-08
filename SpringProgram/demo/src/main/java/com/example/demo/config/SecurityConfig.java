package com.example.demo.config;

import java.util.List;

import com.example.demo.filter.JwtAuthFilter;
import com.example.demo.service.PersonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;


import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
/**
 * Security configuration class for handling authentication and authorization settings.
 * It configures password encoding, authentication providers, and HTTP security settings.
 */
public class SecurityConfig {
    
    /* 
    @Autowired
    private JwtAuthFilter authFilter; */

    /**
     * Provides a password encoder for hashing user passwords.
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures the user details service using the PersonService.
     * @param personService the service responsible for loading user details.
     * @return a UserDetailsService instance.
     */
    @Bean
    public UserDetailsService userDetailsService(PersonService personService){
        return personService;
    }

    /**
     * Configures the authentication provider using DAO-based authentication.
     * @param personService the service responsible for retrieving user details.
     * @return an instance of AuthenticationProvider.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PersonService personService){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(personService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Configures the authentication manager.
     * @param config the AuthenticationConfiguration instance.
     * @throws Exception if an error occurs while retrieving the authentication manager.
     * @return an instance of AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    /**
     * Configures security settings, including authentication rules and session management.
     * @param http the HttpSecurity instance.
     * @param authenticationProvider the authentication provider to use.
     * @param authFilter the JWT authentication filter.
     * @throws Exception if an error occurs during configuration.
     * @return a SecurityFilterChain instance.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider, JwtAuthFilter authFilter) throws Exception {
        http
            .cors().and() //TODO fix this, currently blocked if not included
            .csrf(csrf -> csrf.disable())  // New way to disable CSRF in Spring Security 6.1+
            .authorizeHttpRequests(auth -> auth
                //.anyRequest().permitAll()  // Allows all endpoints without authentication (for testing)
                .requestMatchers("/person/register", "/person/updateApplicant", "/auth/generateToken**","/person/requestApplicantReset","/translation/**").permitAll()
                .requestMatchers("/application/**").hasAuthority("applicant")
                .requestMatchers("/review/**", "/person/updateReviwer", "/person/find", "/person/findPerson").hasAuthority("recruiter")
                .anyRequest().authenticated() 
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider).addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
