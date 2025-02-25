package com.example.demo.presentation.restException;

/**
 * This class implements a general error, if a database access failed for some reason
 */
public class CustomDatabaseException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the application could not be updated
     */
    public CustomDatabaseException() {
        super("Failed due to database error, please try again");
      }
}