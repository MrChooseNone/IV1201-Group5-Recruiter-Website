package com.example.demo.presentation.restAdvice;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.presentation.restException.EntryNotFoundExceptions.LanguageNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.TranslationsNotFoundException;

/**
 * This class is responsible for defining the error handeling for the TranslationEndpointController flow, defining which http response code should be used for the different potential errors
 */
@RestControllerAdvice
public class TranslationRestAdvice {
  /**
   * This function is responsible for handeling the LanguageNotFound error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the LanguageNotFound error message as the text
   */
  @ExceptionHandler(LanguageNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String LanguageNotFoundExceptionHandler(LanguageNotFoundException ex) {
    return ex.getMessage();
  }

  /**
   * This function is responsible for handeling the TranslationsNotFoundException error
   * @param ex the error which was thrown to active this handler
   * @return this sends a http 404 error message with the TranslationsNotFoundException error message as the text
   */
  @ExceptionHandler(TranslationsNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String TranslationsNotFoundExceptionHandler(TranslationsNotFoundException ex) {
    return ex.getMessage();
  }
}
