package com.example.demo.repository;

import com.example.demo.domain.entity.Competence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The RoleRepository interface is responsible for providing CRUD operations for the Role entity.
 */
@Repository
public interface CompetenceRepository extends JpaRepository<Competence, Integer>{
    Competence findByName(String name);
}
