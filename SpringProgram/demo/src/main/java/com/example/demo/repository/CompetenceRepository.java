package com.example.demo.repository;

import com.example.demo.domain.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;


/**
 * The CompetenceRepository interface is responsible for providing CRUD operations for the Competence entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a roleback being impossible
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
//Note that the above means that any explicitly defined methods for this interface requier being called inside a transaction
//Non-explicitly defined ones, aka inherited ones from JPARepository do not requier this, however note that they should still be called inside of a transaction to avoid roleback issues
public interface CompetenceRepository extends JpaRepository<Competence, Integer>{
    /**
     * This interface tells JPA to generate a query to find one competence where the name column equals the name parameter.
     * @param name the language to search for translations for
     * @return A competence whose name matches the name parameter
     */
    Competence findByName(String name);
}
