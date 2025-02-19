package com.example.demo.domain.entity;

import java.sql.Date;

import com.example.demo.domain.dto.AvailabilityDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;

@Entity
/**
 * Represents a availability entity in the system.
 * Implements the AvailabilityDTO interface.
 */
public class Availability implements AvailabilityDTO{
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "availability_id")
    private Integer availabilityId;

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    private Person person;

    @Column(name="from_date")
    private Date fromDate;

    @Column(name="to_date")
    private Date toDate;

    /**
     * Default constructor for the Availability class
     */
    public Availability(){};

    /**
     * This is a constructor with the person, fromDate and toDate parameters
     * @param person
     * @param fromDate
     * @param toDate
     */
    public Availability(Person person, Date fromDate, Date toDate)
    {
        this.person=person;
        this.fromDate=fromDate;
        this.toDate=toDate;
    };

    /**
     * Implements the AvailabilityDTO function getAvailabilityId, and returns the availability id
     * @return the availability id
     */
    @Override
    public Integer getAvailabilityId(){return this.availabilityId;}

    /**
     * Implements the AvailabilityDTO function getPerson, and returns the person this period refers to
     * @return the person
     */
    @Override
    public Person getPerson(){return this.person;}

    /**
     * Implements the AvailabilityDTO function getFromDate, and returns the start date of the period
     * @return the start date of the period
     */
    @Override
    public Date getFromDate(){return this.fromDate;}
    
    /**
     * Implements the AvailabilityDTO function getToDate, and returns the end date of the period
     * @return the end date of the period
     */
    @Override
    public Date getToDate(){return this.toDate;}


    /**
     * This is a setter for availabilityId, IT SHOULD ONLY BE USED IN TESTING!
     * @param newId the new availability id
     */
    public void setAvailabilityId(Integer newId){this.availabilityId=newId;}

}
