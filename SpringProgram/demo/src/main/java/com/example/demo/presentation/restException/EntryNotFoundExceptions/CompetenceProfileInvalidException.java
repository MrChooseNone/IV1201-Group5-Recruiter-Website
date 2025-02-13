package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a specific error for the ApplicationService.java class, specifically if a competence profile does not exist
 */
public class CompetenceProfileInvalidException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the competence profile could not be found
     */
    public CompetenceProfileInvalidException(String cause) {
        super("Competence profile invalid due to : " + cause);
      }
}