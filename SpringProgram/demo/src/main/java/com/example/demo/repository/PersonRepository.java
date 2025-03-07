package com.example.demo.repository;

import com.example.demo.domain.entity.Person;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The PersonRepository interface is responsible for providing CRUD operations for the Person entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a rollback being impossible
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface PersonRepository extends JpaRepository<Person, Integer>{
    /**
     * This interface function tells JPA to generate a query finding a person by the value of the name parameter
     * @param name The name of the person to find
     * @return A list of Person objects with the specified name
     */
    List<Person> findByName(String name);

    /**
     * Finds a person by their email
     * @param email The email of the person
     * @return An Optional containing the person, if found
     */
    Optional<Person> findByEmail(String email);

    /**
     * Finds a person by their username
     * @param username The username of the person
     * @return An Optional containing the person, if found
     */
    Optional<Person> findByUsername(String username);

    /**
     * Finds a person by their personal identity number
     * @param pnr The personal identity number of the person
     * @return An Optional containing the person, if found
     */
    Optional<Person> findByPnr(String pnr);

    /**
     * Checks if a person with the given email already exists
     * @param email The email to check
     * @return True if an account with the email exists, otherwise false
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a person with the given username already exists
     * @param username The username to check
     * @return True if an account with the username exists, otherwise false
     */
    boolean existsByUsername(String username);

    /**
     * Checks is a person with the same personal identity numbers already exists
     * @param pnr The personal identity number to check
     * @return True if an account with the personal identity number exists, otherwise false
     */
    boolean existsByPnr(String pnr);
}