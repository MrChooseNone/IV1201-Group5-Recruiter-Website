package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a specific error for the ReviewService.java class, specifically if an application does not exist
 */
public class ApplicationNotFoundException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the application could not be found
     */
    public ApplicationNotFoundException(String cause) {
        super("Could not find any matching application due to : " + cause);
      }
}
