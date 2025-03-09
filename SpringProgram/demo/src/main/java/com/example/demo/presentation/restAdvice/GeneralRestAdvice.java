package com.example.demo.presentation.restAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.AlreadyExistsException;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.InvalidJWTException;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.InvalidPersonException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.SpecificCompetenceNotFoundException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.security.core.AuthenticationException;


/**
 * This class is responsible for defining the error handeling general errors, such as invalid parameter exceptions
 */
@RestControllerAdvice
@Order//This ensures this is loaded after the other rest advices, link https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/annotation/Order.html 
public class GeneralRestAdvice {

  //Here we create the logger
  private static final Logger LOGGER = LoggerFactory.getLogger(GeneralRestAdvice.class.getName()); 

  /**
   * This function is responsible for handeling the InvalidParameterException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 status code with the InvalidParameterException error message as the text
   */
  @ExceptionHandler(InvalidParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String specificCompetenceNotFoundHandler(InvalidParameterException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the SpecificCompetenceNotFound error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 status code with the SpecificCompetenceNotFound error message as the text
   */
  @ExceptionHandler(SpecificCompetenceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String specificCompetenceNotFoundHandler(SpecificCompetenceNotFoundException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the PersonNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 status code with the PersonNotFoundException error message as the text
   */
  @ExceptionHandler(PersonNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String PersonNotFoundHandler(PersonNotFoundException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the AlreadyExistsException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 409 status code with the AlreadyExistsException error message as the text
   */
  //409 is used since the request was correct, but it could not be fulfilled
  @ExceptionHandler(AlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String AlreadyExistsExceptionHandler(AlreadyExistsException ex) {
    return ex.getMessage();
  }

        /**
   * This function is responsible for handeling the InvalidPersonException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 status code with the InvalidPersonException error message as the text
   */
  @ExceptionHandler(InvalidPersonException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String InvalidPersonExceptionHandler(InvalidPersonException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the CustomDatabaseException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 500 status code with the CustomDatabaseException error message as the text
   */
  @ExceptionHandler(CustomDatabaseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String CustomDatabaseExceptionHandler(CustomDatabaseException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the ConstraintViolationException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 status code with a custom error message based on the ConstraintViolationException's 
   */
  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String ConstraintViolationExceptionHandler(ConstraintViolationException ex) {

    String errorMessage="Request contained 1 or more incorrectly formatted parameters, the following are details about the exact issues: ";

    for (ConstraintViolation<?> i : ex.getConstraintViolations()) {
      errorMessage+= " \n" + i.getMessage();
    }

    return errorMessage;
  }

  /**
   * This function is responsible for handeling the InvalidJWTException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 status code with the InvalidJWTException error message as the text
   */
  @ExceptionHandler(InvalidJWTException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String InvalidJWTExceptionHandler(InvalidJWTException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the UsernameNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 status code with the UsernameNotFoundException error message as the text
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String UsernameNotFoundExceptionHandler(UsernameNotFoundException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the AuthenticationException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 401 status code with the AuthenticationException error message as the text
   */
  @ExceptionHandler(AuthenticationException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  String UsernameNotFoundExceptionHandler(AuthenticationException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the AuthenticationException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 401 status code with the MissingServletRequestParameterException error message as the text
   */
  @ExceptionHandler(MissingServletRequestParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String MissingServletRequestParameterExceptionHandler(MissingServletRequestParameterException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling every unhandeled error, to ensure the server does not crash and always atleast returns something to the client
   * Note this should generally never be called in normal operations, and exists mostly to ensure the client gets the correct response
   * @param ex the error which was thrown to active this handler
   * @return this sends a http INTERNAL_SERVER_ERROR status code with the Exception error message as the text
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String ExceptionHandler(Exception ex) {
    LOGGER.error("An unknown error occured, find exactly why and solve the problem ! : ", ex);
    return "Unhandled error occured, specifically : "+ex.getMessage();
  }

}
