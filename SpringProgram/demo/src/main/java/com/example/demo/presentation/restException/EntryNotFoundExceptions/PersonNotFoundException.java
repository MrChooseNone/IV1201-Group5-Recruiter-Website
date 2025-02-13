package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a general error for if a person could not be found for the specified id
 */
public class PersonNotFoundException extends RuntimeException {

    /**
     * This constructor creates the error, and defines it's error message, which is based on the request id
     * @param id The id for the person this request was attempting to access
     */
    public PersonNotFoundException(Integer id) {
      super("Could not find a person with the following id : " + id);
    }
  }