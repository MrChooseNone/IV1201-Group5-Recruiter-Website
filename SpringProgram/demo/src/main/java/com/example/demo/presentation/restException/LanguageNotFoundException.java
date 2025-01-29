package com.example.demo.presentation.restException;

/**
 * This class implements a specific error for the TranslationService.java class, specifically if a specific language does not exist
 */
public class LanguageNotFoundException extends RuntimeException{
    
    /**
     * This constructor creates the error, and defines it's error message, which is based on the request id
     * @param id The id for the competence this request was attempting to access
     */
    public LanguageNotFoundException(String language) {
      super("Could not find the following language in the database  : " + language);
    }
  }