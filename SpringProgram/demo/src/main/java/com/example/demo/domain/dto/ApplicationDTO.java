package com.example.demo.domain.dto;

import java.util.List;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.entity.CompetenceProfile;

/** 
 * The ApplicationDTO interface provides a data transfer interface for applications
*/
public interface ApplicationDTO {
    /** Interface function for retriving the application id
     * 
     * @return the application id
     */
    public Integer getApplicationId();

    /** Interface function for retriving the applicant
     * 
     * @return the applicant 
     */
    public PersonDTO getApplicant();

    /** Interface function for retriving a list of availability periods for this specific application
     * 
     * @return a list of availability periods for this specific application 
     */
    public List<? extends AvailabilityDTO> getAvailabilityPeriodsForApplication();

    /** Interface function for retriving a list of competence profiles for this specific application
     * 
     * @return a list of competence profiles for this specific application 
     */
    public List<? extends CompetenceProfileDTO> getCompetenceProfilesForApplication();

    /** Interface function for retriving the version number for this application
     * 
     * @return the version number for this application
     */
    public Integer getVersionNumber();

    /** Interface function for retriving the application status for this application
     * 
     * @return the application status for this application
     */
    public ApplicationStatus getApplicationStatus();

}
