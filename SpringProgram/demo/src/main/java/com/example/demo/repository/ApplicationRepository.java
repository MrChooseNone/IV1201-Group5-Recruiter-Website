package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.entity.Application;
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

    /**
     * This method checks if an application already exists with these exact availability periods and for a specific person
     * @param availabilities The list of availabilities to match with
     * @param person The person to find a match for
     * @return A boolean for if the application exists
     */
    //Link to website used as basis for writing the query : https://vladmihalcea.com/spring-data-exists-query/ 
    //Stack overflow page used to learn about list parameters https://stackoverflow.com/questions/65298257/how-to-use-list-parameter-in-jpa-query-with-spring-data-rest

    Boolean existsByAvailabilityPeriodsForApplicationAndApplicant(@Param("availabiltyIds") List<Integer> availabilityIds, Person applicant);
    @Query(value = """
        SELECT * FROM 
        (
            SELECT 
            CASE
                WHEN count >= :length THEN 'true'
            ELSE 'false'
            END AS result
            FROM (
                SELECT COUNT(application_availability_periods.availability_id) AS count
                FROM application
                INNER JOIN application_availability_periods ON application.application_id = application_availability_periods.application_id
                WHERE availability_id IN :availabiltyIds AND application.person_id = :person_id
                GROUP BY application.application_id
                ORDER BY COUNT(application_availability_periods.availability_id) desc
            )
        )        
        ORDER BY result DESC
        LIMIT 1;
    """,
    nativeQuery = true)
    Boolean isListFullyReusedForAPerson(@Param("availabiltyIds") List<Integer> availabilityIds, @Param("length") Integer lengthOfArray, @Param("person_id") Integer personId);

    @Query(value = """
        SELECT 
        CASE
            WHEN count = :length THEN 'true'
        ELSE 'false'
        END AS result
        FROM (
            SELECT COUNT(application_availability_periods.availability_id) AS count
            FROM application
            INNER JOIN application_availability_periods ON application.application_id = application_availability_periods.application_id
            GROUP BY application.application_id
            ORDER BY COUNT(application_availability_periods.availability_id) desc
        );
    """,
    nativeQuery = true)
    boolean isListFullyReused(@Param("availabiltyIds") Object[] availabilityIds, @Param("length") Integer lengthOfArray);

    @Query(value = """
        SELECT COUNT(application_availability_periods.availability_id)
        FROM application
        INNER JOIN application_availability_periods ON application.application_id = application_availability_periods.application_id
        WHERE availability_id IN :availabiltyIds
        GROUP BY application.application_id
        ORDER BY COUNT(application_availability_periods.availability_id) desc;
    """,
    nativeQuery = true
    )
    List<Integer> countOfReusedAvailability(@Param("availabiltyIds") Object[] availabilityIds);
}