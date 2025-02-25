package com.example.demo.presentation.restAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.AlreadyExistsException;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.InvalidPersonException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.SpecificCompetenceNotFoundException;

/**
 * This class is responsible for defining the error handeling general errors, such as invalid parameter exceptions
 */
@RestControllerAdvice
public class GeneralRestAdvice {



    /**
   * This function is responsible for handeling the InvalidParameterException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the InvalidParameterException error message as the text
   */
  @ExceptionHandler(InvalidParameterException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String specificCompetenceNotFoundHandler(InvalidParameterException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the SpecificCompetenceNotFound error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the SpecificCompetenceNotFound error message as the text
   */
  @ExceptionHandler(SpecificCompetenceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String specificCompetenceNotFoundHandler(SpecificCompetenceNotFoundException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the PersonNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the PersonNotFoundException error message as the text
   */
  @ExceptionHandler(PersonNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String PersonNotFoundHandler(PersonNotFoundException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the AlreadyExistsException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 409 error message with the AlreadyExistsException error message as the text
   * 
   */
  //409 is used since the request was correct, but it could not be fulfilled
  @ExceptionHandler(AlreadyExistsException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String PersonNotFoundHandler(AlreadyExistsException ex) {
    return ex.getMessage();
  }

        /**
   * This function is responsible for handeling the InvalidPersonException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the InvalidPersonException error message as the text
   */
  @ExceptionHandler(InvalidPersonException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  String InvalidPersonExceptionHandler(InvalidPersonException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the CustomDatabaseException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 500 error message with the CustomDatabaseException error message as the text
   */
  @ExceptionHandler(CustomDatabaseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  String CustomDatabaseExceptionHandler(CustomDatabaseException ex) {
    return ex.getMessage();
  }
}
