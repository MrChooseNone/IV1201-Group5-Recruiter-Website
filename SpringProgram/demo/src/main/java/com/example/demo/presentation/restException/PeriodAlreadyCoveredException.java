package com.example.demo.presentation.restException;

import java.sql.Date;

/**
 * This class implements a specific error for the ApplicationService class,
 * specifically if a availability period is fully covered by an existing range
 */
public class PeriodAlreadyCoveredException extends RuntimeException {
    /**
     * This constructor creates the error, and defines it's error message, which is based on the cause
     * 
     * @param cause A description of why the application could not be updated
     */
    public PeriodAlreadyCoveredException(Date fromDate, Date toDate) {
        super("Could not create availability period since range start date " + fromDate.toString() + " to end date "+ toDate.toString()+" is fully covered by an existing availability period ");
    }
}