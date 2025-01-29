package com.example.demo.repository;

import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.entity.Language;

/**
 * The LanguageRepository interface is responsible for providing CRUD operations for the Language entity.
 */
@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer>{
    /**
     * This interface function tells JPA to generate a query finding one Language by the value of the name parameter
     * @param name The name of the languge to find
     * @return The String object with the specified name
     */
    Language findByName(String name);
}
