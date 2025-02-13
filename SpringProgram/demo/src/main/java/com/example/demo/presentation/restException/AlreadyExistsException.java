package com.example.demo.presentation.restException;

/**
 * This class implements a general error, for if a resource whose creation is requested already exists
 */
public class AlreadyExistsException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the application could not be found
     */
    public AlreadyExistsException(String cause) {
        super("The requested resource already exists : " + cause);
      }
}