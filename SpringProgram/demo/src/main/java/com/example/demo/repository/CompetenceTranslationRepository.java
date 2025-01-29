package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;

/**
 * The CompetenceTranslationRepository interface is responsible for providing CRUD operations for the CompetenceTranslation entity.
 */
public interface CompetenceTranslationRepository extends JpaRepository<CompetenceTranslation, Integer>{
    /**
     * This interface tells JPA to generate a query to find all translations where the language_id column equals the languageId parameter.
     * Relevant link for handeling multi-result JPA queries : https://stackoverflow.com/questions/22813539/spring-data-jpa-repository-findall-method-using-flag-column
     * @param languageId the id for the language to search for translations for
     * @return A list of all translations for this language
     */
    List<CompetenceTranslation> findByLanguage_id(Integer languageId);
}
