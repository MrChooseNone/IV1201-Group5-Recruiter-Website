package com.example.demo.presentation.restException;

import java.sql.Date;

/**
 * This class implements a specific error for the ReviewService.java class,
 * specifically if an application could not be updated
 */
public class FromDateAfterToDateException extends RuntimeException {
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * 
     * @param cause A description of why the application could not be updated
     */
    public FromDateAfterToDateException(Date fromDate, Date toDate) {
        super("Could not create availability period since start date " + fromDate.toString() + " is after end date "+ toDate.toString());
    }
}