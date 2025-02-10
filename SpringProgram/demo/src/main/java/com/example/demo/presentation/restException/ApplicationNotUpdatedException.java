package com.example.demo.presentation.restException;

/**
 * This class implements a specific error for the ReviewService.java class, specifically if an application could not be updated
 */
public class ApplicationNotUpdatedException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the application could not be updated
     */
    public ApplicationNotUpdatedException(String cause) {
        super("Could not update application due to : " + cause);
      }
}