package com.example.demo.domain.entity;

import com.example.demo.domain.dto.PersonDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * Represents a person entity in the system.
 * Implements the PersonDTO interface.
 */
@Entity
public class Person implements PersonDTO{
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "person_id")
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="pnr")
    private String pnr;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne
    private Role role;

    /**
     * Default constructor.
     */
    public Person() {
    }

    /**
     * Implements the PersonDTO function getId, and returns the person objects id
     * @return the person's id
     */
    @Override
    public Integer getId() {
        return this.id;
    }

    /**
     * Implements the PersonDTO function getName, and returns the person objects first name
     * @return the person's first name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Implements the PersonDTO function setName, and sets the person object's first name
     * @param firstName the first name to set
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Implements the PersonDTO function getSurname, and returns the person objects last name
     * @return the person's last name
     */
    @Override
    public String getSurname() {
        return this.surname;
    }

    /**
     * Implements the PersonDTO function setSurname, and sets the person objects last name
     * @param surname the person's last name
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Implements the PersonDTO function getPnr, and returns the person objects personal identity number
     * @return the person's identity number
     */
    @Override
    public String getPnr() {
        return this.pnr;
    }

    /**
     * Implements the PersonDTO function getEmail, and returns the person objects personal email
     * @return the person's email
     */
    @Override
    public String getEmail() {
        return this.email;
    }

    /**
     * Implements the PersonDTO function getPassword, and returns the person objects password
     * @return the person's password
     */
    @Override
    @JsonIgnore //This annotation ensures this property is ignored when returning the object to users, avoiding sharing the password with everyone with indirect access to the user
    public String getPassword() {
        return this.password;
    }

    /**
     * Implements the PersonDTO function getRole, and returns the person objects role.
     * @return the person's role
     */
    public Role getRole() {
        return this.role;
    }

}
