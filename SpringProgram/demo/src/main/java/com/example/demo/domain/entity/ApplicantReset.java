package com.example.demo.domain.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
/**
 * This represent the data relevant for an applicant requesting an account reset Note this does not implement a DTO,  since it should NEVER leave the service layer and be returned to a user, since that could allow a user to hijack anothers account
 */
public class ApplicantReset {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reset_id")
    private Integer id;

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    @NotNull(message="Each applicant account reset must be for a specific person")
    private Person person;

    //Note that this is string since we only want exact matches, and this allows the PersonDetails to use the more detailed java.util.date format over the less specific java.sql.date
    @NotNull(message = "Each reset must have a specific expiration date")
    @NotBlank(message="Each expiration date much not be empty")
    @Column()
    private String resetDate;

    @NotNull(message = "Each reset must have a specific random number")
    @Column()
    private Long randomLong;

    /**
     * This is a constructor for ApplicantReset
     */
    public ApplicantReset()
    {}

    /**
     * This is a full ags constructor for ApplicantReset
     */
    public ApplicantReset(Person person, String resetDate, Long randomLong)
    {
        this.person=person;
        this.resetDate=resetDate;
        this.randomLong=randomLong;
    }

    /**
     * This is a getter for the reset id attribute
     * @return this instances id
     */
    public Integer getResetId()
    {
        return this.id;
    }

    /**
     * This is a getter for the person attribute
     * @return The instances person
     */
    public Person getPerson()
    {
        return this.person;
    }

    /**
     * This is a setter for the person attribute
     * @param newPerson the new person
     */
    public void setPerson(Person newPerson)
    {
        this.person=newPerson;
    }

    /**
     * This is a getter for the resetDate attribute
     * @return The instances resetDate attribute
     */
    public String getResetDate()
    {
        return this.resetDate;
    }

    /**
     * This is a setter for the resetDate attribute
     * @param newResetDate the new date
     */
    public void setResetDate(String newResetDate)
    {
        this.resetDate=newResetDate;
    }

    /**
     * This is a getter for the randomLong attribute
     * @return The instances resetDate attribute
     */
    public Long getRandomLong()
    {
        return this.randomLong;
    }

    /**
     * This is a setter for the randomLong attribute
     * @param newRandomLong the new random number
     */
    public void setRandomLong(Long newRandomLong)
    {
        this.randomLong=newRandomLong;
    }
}
