package com.example.demo.repository;

import com.example.demo.domain.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The Person interface is responsible for providing CRUD operations for the Person entity.
 */
@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
    
}
