package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a general error for if a person is invalid for some reason
 */
public class InvalidPersonException extends RuntimeException {

    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause string
     * @param id The reason the person is invalid
     */
    public InvalidPersonException(String cause) {
      super("Specified person invalid due to : " + cause);
    }
  }