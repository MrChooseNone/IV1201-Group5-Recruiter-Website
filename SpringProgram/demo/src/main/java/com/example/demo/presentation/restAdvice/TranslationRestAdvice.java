package com.example.demo.presentation.restAdvice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import com.example.demo.presentation.restException.SpecificCompetenceNotFound;

/**
 * This class is responsible for defining the error handeling for the TranslationEndpointController, defining which http response code should be used for the different potential errors
 */
@RestControllerAdvice
public class TranslationRestAdvice {

  @ExceptionHandler(SpecificCompetenceNotFound.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String employeeNotFoundHandler(SpecificCompetenceNotFound ex) {
    return ex.getMessage();
  }

}
