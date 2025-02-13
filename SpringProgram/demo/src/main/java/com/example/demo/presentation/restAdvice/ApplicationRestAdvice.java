package com.example.demo.presentation.restAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.FromDateAfterToDateException;

/**
 * This class is responsible for defining the error for ApplicationService.java, such as being unable to create a competence profile
 */
@RestControllerAdvice
public class ApplicationRestAdvice {
        /**
   * This function is responsible for handeling the FromDateAfterToDateException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the FromDateAfterToDateException error message as the text
   */
  @ExceptionHandler(FromDateAfterToDateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String specificCompetenceNotFoundHandler(FromDateAfterToDateException ex) {
    return ex.getMessage();
  }
}
