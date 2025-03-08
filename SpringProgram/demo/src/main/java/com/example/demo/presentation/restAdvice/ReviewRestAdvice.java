package com.example.demo.presentation.restAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.ApplicationNotUpdatedException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.ApplicationNotFoundException;

@RestControllerAdvice
/**
 * This class is responsible for defining the error handeling for the ReviewEndpointController flow, defining which http response code should be used for the different potential errors
 */
public class ReviewRestAdvice {
    /**
   * This function is responsible for handeling the ApplicationNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the ApplicationNotFoundException error message as the text
   */
  @ExceptionHandler(ApplicationNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String ApplicationNotFoundExceptionHandler(ApplicationNotFoundException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the ApplicationNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 409 error message with the ApplicationNotFoundException error message as the text
   * 
   */
  @ExceptionHandler(ApplicationNotUpdatedException.class)
  @ResponseStatus(HttpStatus.CONFLICT) //Link to http code description https://www.rfc-editor.org/rfc/rfc7231#section-6.5.8
  String ApplicationNotUpdatedExceptionHandler(ApplicationNotUpdatedException ex) {
    return ex.getMessage();
  }

}
