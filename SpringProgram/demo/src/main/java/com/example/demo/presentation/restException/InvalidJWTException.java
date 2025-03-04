package com.example.demo.presentation.restException;

/**
 * This class implements a general error, for if token is invalid for some reason
 */
public class InvalidJWTException extends RuntimeException{
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * @param cause A description of why the token was invalid
     */
    public InvalidJWTException(String cause) {
        super("The provided token is invalid due to : " + cause);
      }
}