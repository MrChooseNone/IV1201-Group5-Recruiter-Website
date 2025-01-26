package com.example.demo.domain.entity;

import java.sql.Date;

import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.PersonDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
     * Implements the AvailabilityDTO function GetAvailabilityId, and returns the availability id
     * @return the availability id
     */
    @Override
    public Integer GetAvailabilityId(){return this.availabilityId;}

    /**
     * Implements the AvailabilityDTO function GetPerson, and returns the person this period refers to
     * @return the person
     */
    @Override
    public Person GetPerson(){return this.person;}

    /**
     * Implements the AvailabilityDTO function GetFromDate, and returns the start date of the period
     * @return the start date of the period
     */
    @Override
    public Date GetFromDate(){return this.fromDate;}
    
    /**
     * Implements the AvailabilityDTO function GetToDate, and returns the end date of the period
     * @return the end date of the period
     */
    @Override
    public Date GetToDate(){return this.toDate;}

}
