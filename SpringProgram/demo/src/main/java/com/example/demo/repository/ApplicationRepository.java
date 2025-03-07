package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Person;

import org.springframework.transaction.annotation.Propagation;


/**
 * The ApplicationRepository interface is responsible for providing CRUD operations for the Application entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a rollback not being performed correctly
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
//Note that the above means that any explicitly defined methods for this interface requier being called inside a transaction
//Non-explicitly defined ones, aka inherited ones from JPARepository do not requier this, however note that they should still be called inside of a transaction to avoid rollback issues
public interface ApplicationRepository extends JpaRepository<Application, Integer>{
    /**
     * This interface tells JPA to generate a query to find a list of Applications with a specific application status equal to the parameter
     * @param applicationStatus the application status to find applications matching
     * @return A list of matching applications
     */
    List<Application> findAllByApplicationStatus(ApplicationStatus applicationStatus);

    /** This interface tells JPA to generate a query to find a application with the specific id
     * @param applicationId the application status to find applications matching
     * @return The matching application, or null if none exists
     */
    Application findByApplicationId(Integer applicationId);

    /**
     * This method checks if an application already exists with these exact availability periods and for a specific person
     * @param availabilities The list of availabilities to match with
     * @param person The person to find a match for
     * @return A boolean for if the application exists
     */
    Boolean existsByAvailabilityPeriodsForApplicationAndApplicant(List<Availability> availabilityPeriodsForApplication, Person applicant);
}