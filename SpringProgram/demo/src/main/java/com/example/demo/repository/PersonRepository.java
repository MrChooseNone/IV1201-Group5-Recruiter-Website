package com.example.demo.repository;

import com.example.demo.domain.entity.Person;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * The RoleRepository interface is responsible for providing CRUD operations for the Role entity.
 */
@Repository
@Transactional(propagation = Propagation.MANDATORY)
public interface PersonRepository extends JpaRepository<Person, Integer>{
        /**
     * This interface function tells JPA to generate a query finding one Role by the value of the name parameter
     * @param name The name of the languge to find
     * @return The String object with the specified name
     */
    List<Person> findByName(String name);
}