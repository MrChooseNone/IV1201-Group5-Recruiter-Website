package com.example.demo.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;

import org.springframework.transaction.annotation.Propagation;


/**
 * The AvailabilityRepository interface is responsible for providing CRUD operations for the Availability entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a roleback not being performed correctly
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
//Note that the above means that any explicitly defined methods for this interface requier being called inside a transaction
//Non-explicitly defined ones, aka inherited ones from JPARepository do not requier this, however note that they should still be called inside of a transaction to avoid roleback issues
//Link to documentation: https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation 
public interface AvailabilityRepository extends JpaRepository<Availability, Integer> {
    /**
     * This interface tells JPA to generate a query to find a list of Availability for a specific person
     * @param person the person status to find availability periods for
     * @return A list of matching Availability
     */
    List<Availability> findAllByPerson(Person person);

    /**
     * This interface function tells JPA to generate a query to which confirms if an Availability which is fully covered within a specified date range and for a specific person exists
     * @param fromDate start of the date range
     * @param toDate end of the date range
     * @param person the person to find availability periods for
     * @return A boolean representing if it exists or not
     */
    boolean existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(Date fromDate, Date toDate,Person person);

    /**
     * This interface function tells JPA to generate a query which confirms if an Availability already exists with these exact values
     * @param fromDate start of the date range
     * @param toDate end of the date range
     * @param person the person to a availability period for
     * @return A boolean representing if it exists or not
     */
    boolean existsByFromDateAndToDateAndPerson(Date fromDate, Date toDate,Person person);

}