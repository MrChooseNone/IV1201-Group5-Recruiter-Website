package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.CompetenceTranslation;

/**
 * The CompetenceTranslationRepository interface is responsible for providing CRUD operations for the CompetenceTranslation entity.
 * Any explicitly defined functions can only be called inside a transaction, however JPARepository inherited functions can be called outside of one
 * This should however not be done, since doing so could risk a rollback being impossible
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
//Note that the above means that any explicitly defined methods for this interface requier being called inside a transaction
//Non-explicitly defined ones, aka inherited ones from JPARepository do not requier this, however note that they should still be called inside of a transaction to avoid roleback issues
public interface CompetenceTranslationRepository extends JpaRepository<CompetenceTranslation, Integer>{
    /**
     * This interface tells JPA to generate a query to find all translations where the language_id column equals the languageId parameter.
     * Relevant link for handeling multi-result JPA queries : https://stackoverflow.com/questions/22813539/spring-data-jpa-repository-findall-method-using-flag-column
     * @param languageId the id for the language to search for translations for
     * @return A list of all translations for this language
     */
    List<CompetenceTranslation> findByLanguage_id(Integer languageId);
}
