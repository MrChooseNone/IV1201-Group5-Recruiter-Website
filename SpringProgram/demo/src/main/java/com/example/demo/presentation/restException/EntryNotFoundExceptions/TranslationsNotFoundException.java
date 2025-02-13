package com.example.demo.presentation.restException.EntryNotFoundExceptions;

/**
 * This class implements a specific error for the TranslationService.java class, specifically if no translations for a specific language exist 
 */
public class TranslationsNotFoundException extends RuntimeException {

    /**
     * This constructor creates the error, and defines it's error message, which is based on the request id
     * @param language The name for the language this request was attempting to find translation(s) for
     */
    public TranslationsNotFoundException(String language) {
      super("Could not find any translation for the language : " + language);
    }
  }