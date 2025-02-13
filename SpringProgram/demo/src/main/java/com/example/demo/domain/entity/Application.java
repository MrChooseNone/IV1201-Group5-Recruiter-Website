package com.example.demo.domain.entity;

import java.sql.Date;
import java.util.List;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.ApplicationDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;

@Entity
/**
 * Represents a application entity in the system.
 * Implements the ApplicationDTO interface.
 */
public class Application implements ApplicationDTO{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "application_id")
    private Integer applicationId;

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    private Person applicant;

    @JoinColumn(name = "applicationId", referencedColumnName  = "application_id")
    @OneToMany //TODO Change to many-to-many if the same availability period should be usable in multiple applications
    private List<Availability> availabilityPeriodsForApplication;

    @JoinColumn(name = "applicationId", referencedColumnName  = "application_id")
    @OneToMany  //TODO Change to many-to-many if the same competence profile should be usable in multiple applications
    private List<CompetenceProfile> competenceProfilesForApplication;

    @Version //Note that this is what marks this as a version number, and will be used to handle the multi-reviewer update scenario¨
    //Link to discussion about this: https://stackoverflow.com/questions/2572566/java-jpa-version-annotation 
    @Column(name="application_version_number")
    private long versionNumber;

    @Enumerated(EnumType.STRING) //This specifies how the enum should be saved in the database, 
    @Column(name="application_status")
    private ApplicationStatus applicationStatus;

    @Column(name="application_date")
    private Date applicationDate;

    /** 
     * This is the default constructor for Application
    */
    public Application(){}


    /**
     * This is a constructor which accepts person, availabilityPeriodsForApplication and competenceProfilesForApplication parameters and automatically sets application status and date
     * @param applicant This is the applicant
     * @param availabilityPeriodsForApplication This is a list of availability periods for this application
     * @param competenceProfilesForApplication This is a list of competence profiles for this application
     */
    public Application(Person applicant, List<Availability> availabilityPeriodsForApplication,List<CompetenceProfile> competenceProfilesForApplication){
        this.applicant=applicant;
        this.availabilityPeriodsForApplication=availabilityPeriodsForApplication;
        this.competenceProfilesForApplication=competenceProfilesForApplication;
        this.applicationStatus=ApplicationStatus.unchecked;
        this.applicationDate=new java.sql.Date(System.currentTimeMillis());
    }

    /**
    * Implements the ApplicationDTO function getApplicationId, and returns the application id
    * @return the application id
    */
    @Override
    public Integer getApplicationId() {
        return this.applicationId;
    }

    /**
    * Implements the ApplicationDTO function getApplicant, and returns the applicant
    * @return the applicant
    */
    @Override
    public Person getApplicant() {
        return this.applicant;
    }

    /**
    * Implements the ApplicationDTO function getAvailabilityPeriodsForApplication, and returns a list of availability periods for this application
    * @return the list of availability periods for this application
    */
    @Override
    public List<Availability> getAvailabilityPeriodsForApplication() {
        return this.availabilityPeriodsForApplication;
    }

    /**
    * Implements the ApplicationDTO function getCompetenceProfilesForApplication, and returns a list of competence profiles for this application
    * @return the list of competence profiles for this application
    */
    @Override
    public List<CompetenceProfile> getCompetenceProfilesForApplication() {
        return this.competenceProfilesForApplication;
    }

    /**
    * Implements the ApplicationDTO function getVersionNumber, and returns the version number for this version of the application
    * @return the version number
    */
    @Override
    public long getVersionNumber() {
        return this.versionNumber;
    }

    /**
    * Implements the ApplicationDTO function getApplicationStatus, and returns the application status
    * @return the application status
    */
    @Override
    public ApplicationStatus getApplicationStatus() {
        return this.applicationStatus;
    } 

    /**
    * Implements the ApplicationDTO function getApplicationDate, and returns the application date
    * @return the date the application was sent 
    */
    @Override
    public Date getApplicationDate()
    {
        return this.applicationDate;
    }

    /**
     * This is a setter for application status
     * @param newStatus the new status for this application
     */
    public void setApplicationStatus(ApplicationStatus newStatus)
    {
        this.applicationStatus=newStatus;
    }

    /**
     * This is a setter for the applicant
     * @param newApplicant the new applicant for this application
     */
    public void setApplicant(Person newApplicant)
    {
        this.applicant=newApplicant;
    }

    /**
     * This is a setter for application date, this is used by loader and could be used to handle applicant re-submission if that is implemented in the future
     * @param newStatus the new applicantion date 
     */
    public void setApplicationDate(Date applicationDate)
    {
        this.applicationDate=applicationDate;
    }
}
