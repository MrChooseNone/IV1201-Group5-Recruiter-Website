package com.example.demo.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.entity.ApplicantReset;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Person;

/**
 * The ApplicationRepository interface is responsible for providing CRUD operations for the ApplicantReset entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a roleback not being performed correctly
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
//Note that the above means that any explicitly defined methods for this interface requier being called inside a transaction
//Non-explicitly defined ones, aka inherited ones from JPARepository do not requier this, however note that they should still be called inside of a transaction to avoid roleback issues
public interface ApplicantResetRepository extends JpaRepository<ApplicantReset, Integer>{
    
    /**
     * This interface function tells JPA to generate a query to find if the specific person has requested an applicant reset
     * @param person The person to find a reset for
     * @param date The date to find a request for
     * @param randomLong the random long to find a match with
     * @return A boolean representing if a reset request existed or not
     */
    public Optional<ApplicantReset> findByPersonAndResetDateAndRandomLong(Person person, String resetDate, Long randomLong);
}
