package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {
    
    /**
     * Provides a password encoder for hashing user passwords.
     * @return BCryptPasswordEncoder instance.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configures security settings to allow public access to all endpoints for testing.
     * @param http The HttpSecurity configuration.
     * @return SecurityFilterChain instance.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // ✅ New way to disable CSRF in Spring Security 6.1+
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()  // ✅ Allows all endpoints without authentication (for testing)
            );

        return http.build();
    }
}
