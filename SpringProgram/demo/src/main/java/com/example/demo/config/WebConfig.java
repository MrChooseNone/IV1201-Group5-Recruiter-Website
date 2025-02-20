package com.example.demo.config;

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
    
    @Override
    /**
     * This function tells spring to allow cross-origin access to everything from http://localhost:3000, which is reacts development server port, for the purpose of development
     */
    public void addCorsMappings(@NonNull CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000/")  // React's development server URL
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Allowed HTTP methods
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}

