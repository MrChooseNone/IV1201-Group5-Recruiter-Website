package com.example.demo.presentation.restException;

/**
 * This class implements a specific error for the TranslationService.java class, specifically if a specific competence does not exist
 */
public class PersonNotFoundException extends RuntimeException {

    /**
     * This constructor creates the error, and defines it's error message, which is based on the request id
     * @param id The id for the person this request was attempting to access
     */
    public PersonNotFoundException(Integer id) {
      super("Could not find specific person with id : " + id);
    }
  }