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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

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
    @NotNull(message = "Name must not be null")
    @NotBlank(message="Name can not be an empty string")
    private String name;

    @Column(name="surname")
    @NotNull(message = "Surname should not be null")
    @NotBlank(message="Surname can not be an empty string")
    private String surname;

    @Column(name="pnr")
    @Pattern(regexp="\\d{8}-\\d{4}", message = "Pnr must follow the following format yyyymmdd-nnnn, with each char replaced with appropriate numbers") //Not the same regex as frontend
    @NotBlank(message="Pnr may not be an empty string")
    @NotNull(message = "Pnr may not be null")
    private String pnr;

    @Column(name="email")
    @Email(message = "Email must be a possible email address, following the format something@something.something, with something being any alphanumeric text")
    @NotBlank(message="Email may not be an empty string")
    @NotNull(message="Email must not be null")
    private String email;

    @Column(name="password")
    @NotNull(message = "Password must not be null")
    @NotBlank(message="Password can not be an empty string")
    private String password;

    @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    @ManyToOne
    @NotNull(message = "Each person must have a role")
    private Role role;

    @Column(name="username")
    @NotNull(message = "Username must not be null")
    @NotBlank(message="Username can not be an empty string")
    private String username;

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
     * Basic settet for the id, only for testing purposes, DO NOT USE IN PRODUCTION!
     * @param id the new id for the person
     */
    public void setId(Integer id) {
        this.id=id;
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
    @Override
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
     * Implements the PersonDTO function setPnr, and sets the person objects personal identity number
     * @param pnr the person's personal identity number
     */
    @Override
    public void setPnr(String pnr) {
        this.pnr = pnr;
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
     * Implements the PersonDTO function setEmail, and sets the person objects email
     * @param email the person's email
     */
    @Override
    public void setEmail(String email) {
        this.email = email;
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
     * Implements the PersonDTO function setPassword, and sets the person objects password
     * @param password the person's password
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Implements the PersonDTO function getRole, and returns the person objects role
     * @return the person's role
     */
    @Override
    public Role getRole() {
        return this.role;
    }

    /**
     * Setter for the role attribute
     * @param role the new role 
     */
    public void setRole(Role role) {
        this.role=role;
    }

    /**
     * Implements the PersonDTO function getUsername, and returns the person objects username
     * @return the person's username
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * Implements the PersonDTO function setPassword, and sets the person objects password
     * @param password the person's password
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

}
