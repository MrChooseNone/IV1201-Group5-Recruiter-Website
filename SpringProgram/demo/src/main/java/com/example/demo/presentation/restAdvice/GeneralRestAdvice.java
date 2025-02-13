package com.example.demo.presentation.restAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.presentation.restException.PersonNotFoundException;
import com.example.demo.presentation.restException.SpecificCompetenceNotFoundException;

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
}
