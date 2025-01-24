package com.example.demo.domain.entity;

import com.example.demo.domain.dto.dtoExample;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents an example entity in the system.
 */
@Entity
public class entityExample implements dtoExample{

    //The tags below allow the system to perform O-R mappings without explicit information
    /**
     * The identifier for the example database object.
     */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    Integer id;

    //Return function for id
    public Integer getId() {
        return this.id;
    }

    /**
     * Default Constructor
     */
    public entityExample() {}

    /**
     * Constructor with id parameter.
     * 
     * @param id the id
     */
    public entityExample(Integer id) {
        this.id = id;
    }
    
}
