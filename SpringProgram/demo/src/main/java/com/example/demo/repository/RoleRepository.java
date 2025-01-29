package com.example.demo.repository;

import com.example.demo.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The RoleRepository interface is responsible for providing CRUD operations for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
        /**
     * This interface function tells JPA to generate a query finding one Role by the value of the name parameter
     * @param name The name of the languge to find
     * @return The String object with the specified name
     */
    Role findByName(String name);
}
