package com.example.demo.repository;

import com.example.demo.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The RoleRepository interface is responsible for providing CRUD operations for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findByName(String name);
}
