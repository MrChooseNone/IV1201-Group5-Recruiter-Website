package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
/**
 * This is a config file for the website, includes for example config for cross-origin access
 * This is spring managed
 */
public class WebConfig implements WebMvcConfigurer {

    @Value("${ALLOWED_ORIGINS:http://localhost:3000}") // Reads allowed origins from environment variables, defaulting to localhost:3000
    private String allowedOrigins;
    
    @Override
    /**
     * Configures CORS to allow cross-origin access from the specified origins.
     * The allowed origins are fetched from the ALLOWED_ORIGINS environment variable.
     * Multiple origins can be specified as a comma-separated list.
     *
     * @param registry the CORS registry used to configure CORS settings.
     */
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        String[] origins = allowedOrigins.split(","); // Convert comma-separated values to an array

        registry.addMapping("/**")
                .allowedOrigins(origins)  // Use environment-defined origins
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Allowed HTTP methods
                .allowedHeaders("*")  // Allow all headers
                .allowCredentials(true);  // Allow credentials (e.g., cookies, authorization headers)
    }
}

