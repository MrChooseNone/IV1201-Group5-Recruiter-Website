package com.example.demo.domain.entity;

import java.sql.Date;
import java.util.List;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.CompetenceProfileDTO;
import com.example.demo.domain.dto.PersonDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;

public class Application implements ApplicationDTO{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "application_id")
    private Integer applicationId;

    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    @ManyToOne
    private Person applicant;

    @JoinColumn(name = "availability_id", referencedColumnName = "availability_id")
    @OneToMany
    private List<Availability> availabilityPeriodsForApplication;

    @JoinColumn(name = "competence_profile_id", referencedColumnName = "competence_profile_id")
    @OneToMany
    private List<CompetenceProfile> competenceProfilesForApplication;

    @Version //Note that this is what marks this as a version number, and will be used to handle the multi-reviewer update scenario
    @Column(name="application_version_number")
    private int versionNumber;

    @Enumerated(EnumType.STRING) //This specifies how the enum should be saved in the database, 
    @Column(name="application_status")
    private ApplicationStatus applicationStatus;

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
    public Integer getVersionNumber() {
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

}
