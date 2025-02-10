package com.example.demo.service;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.presentation.restException.ApplicationNotFoundException;
import com.example.demo.presentation.restException.ApplicationNotUpdatedException;
import com.example.demo.repository.ApplicationRepository;

import org.springframework.transaction.annotation.Propagation;


@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) //TODO Change to use existing if possible
/**
 * The TranslationService class provides services for endpoints, specifically regarding handling translation (and internationalization)
 * It uses explicit transaction annotation to ensure a rollback occurs whenever an unchecked exception is thrown.
 */
public class ReviewService {
    private final ApplicationRepository applicationRepository;

    /**
     * Constructs a new instance of the ReviewService (Spring boot managed).
     *
     * @param applicationRepository the repository for accessing application database data
     */
    public ReviewService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Returns a list of all applications
     * @return a list of applications
     */
    public List<? extends ApplicationDTO> GetApplications()
    {
        return applicationRepository.findAll();
    }

    /**
     * Returns a list of all applications matching the specified application status
     * @param status the application status to find a match for
     * @return a list of applications matching the status
     */
    public List<? extends ApplicationDTO> GetApplicationsByStatus(ApplicationStatus status)
    {
        return applicationRepository.findAllByApplicationStatus(status);
    }

    /**
     * This function first checks if an application exists with a specific id, and then confirms the application has the correct version number, if so it updates the applicaton status to the specified value and then returns the updated application 
     * @param applicationID The id for the application to update
     * @param newStatus The new application status for the application
     * @param currentVersionNumber The version number of the application
     * @throws ApplicationNotFoundException this is thrown if a matching application can not be found, either due to incorrect id or the version number not being consistent with the application
     * @throws ApplicationNotUpdatedException this is thrown if the application could not be updated due to some business logic, ex new status is the same as existing status
     * @return The updated application
     */
    public ApplicationDTO SetApplicationStatus(Integer applicationID,ApplicationStatus newStatus,Integer currentVersionNumber)
    {
        Optional<Application> applicationToUpdateContainer=applicationRepository.findById(applicationID);
        //This check is for if an application with this id existed or not, if not we throw a specific exception here
        if (applicationToUpdateContainer.isEmpty()) {
            throw new ApplicationNotFoundException("No application with id : \""+applicationID+"\" found");
        }
        Application applicationToUpdate=applicationToUpdateContainer.get();

        //This check is for if the version number of the reviewed version is the same as the current value, if not we throw a specific exception here
        if (applicationToUpdate.getVersionNumber()!=currentVersionNumber) {
            throw new ApplicationNotFoundException("Version number is incorrect, specified : \""+currentVersionNumber+"\" but application with id : \""+applicationID+"\" is on version " + applicationToUpdate.getVersionNumber());
        }

        if (applicationToUpdate.getApplicationStatus()==newStatus) {
            throw new ApplicationNotUpdatedException("application status is already "+newStatus);
        }

        applicationToUpdate.setApplicationStatus(newStatus);
        applicationRepository.save(applicationToUpdate);

        return applicationToUpdate;

    }
}
