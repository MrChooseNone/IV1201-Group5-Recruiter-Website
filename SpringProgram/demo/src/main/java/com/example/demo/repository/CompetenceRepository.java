package com.example.demo.repository;

import com.example.demo.domain.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The CompetenceRepository interface is responsible for providing CRUD operations for the Competence entity.
 */
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer>{
    /**
     * This interface tells JPA to generate a query to find one competence where the name column equals the name parameter.
     * @param name the language to search for translations for
     * @return A competence whose name matches the name parameter
     */
    Competence findByName(String name);
}
