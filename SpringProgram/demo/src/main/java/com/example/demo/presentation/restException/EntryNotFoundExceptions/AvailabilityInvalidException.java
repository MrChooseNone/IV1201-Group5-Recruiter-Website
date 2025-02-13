package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a specific error for the ApplicationService.java class, specifically if a Availability could not be used when creating an application
 */
public class AvailabilityInvalidException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the availability period is invalid
     */
    public AvailabilityInvalidException(String cause) {
        super("Availability invalid due to  : " + cause);
      }
}