package com.example.demo.presentation.restAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.FromDateAfterToDateException;
import com.example.demo.presentation.restException.PeriodAlreadyCoveredException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.AvailabilityInvalidException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.CompetenceProfileInvalidException;

/**
 * This class is responsible for defining the error for ApplicationService.java, such as being unable to create a competence profile
 */
@RestControllerAdvice
@Order(1)//This ensures this is loaded after the other rest advices
public class ApplicationRestAdvice {

    //We create the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationRestAdvice.class.getName()); 


  /**
   * This function is responsible for handeling the FromDateAfterToDateException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the FromDateAfterToDateException error message as the text
   */
  @ExceptionHandler(FromDateAfterToDateException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String FromDateAfterToDateFoundHandler(FromDateAfterToDateException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the FromDateAfterToDateException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the FromDateAfterToDateException error message as the text
   */
  @ExceptionHandler(PeriodAlreadyCoveredException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String PeriodAlreadyCoveredHandler(PeriodAlreadyCoveredException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the CompetenceProfileNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the CompetenceProfileNotFoundException error message as the text
   */
  @ExceptionHandler(CompetenceProfileInvalidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String PeriodAlreadyCoveredHandler(CompetenceProfileInvalidException ex) {
    return ex.getMessage();
  }

    /**
   * This function is responsible for handeling the CompetenceProfileNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the CompetenceProfileNotFoundException error message as the text
   */
  @ExceptionHandler(AvailabilityInvalidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String PeriodAlreadyCoveredHandler(AvailabilityInvalidException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the HttpMessageNotReadableException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 400 error message with the HttpMessageNotReadableException error message as the text
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String PeriodAlreadyCoveredHandler(HttpMessageNotReadableException ex) {
    //TODO research if there is any way to avoid having to log here
    LOGGER.error("Creation of application failed due to parsing error, specifically " +ex.getMessage());

    return ex.getMessage();
  }


  
}
