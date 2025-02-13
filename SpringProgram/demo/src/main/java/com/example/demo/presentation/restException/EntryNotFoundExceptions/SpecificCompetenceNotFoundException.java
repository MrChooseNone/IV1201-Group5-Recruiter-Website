package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a specific error for the TranslationService.java class, specifically if a specific competence does not exist
 */
public class SpecificCompetenceNotFoundException extends RuntimeException {

    /**
     * This constructor creates the error, and defines it's error message, which is based on the request id
     * @param id The id for the competence this request was attempting to access
     */
    public SpecificCompetenceNotFoundException(Integer id) {
      super("Could not find specific competence with id : " + id);
    }
  }