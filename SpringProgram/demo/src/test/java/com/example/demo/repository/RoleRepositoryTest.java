package com.example.demo.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.demo.domain.entity.Role;

import jakarta.validation.ConstraintViolationException;

/**
 * This a unit test of the RoleRepository class
 * Based on a article by Wynn Teo for Baeldung
 * Link: https://www.baeldung.com/junit-datajpatest-repository
 */
@DataJpaTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb", //This specifies the in-memory database url
    "spring.jpa.hibernate.ddl-auto=create-drop" //This is used to specify that the database schema should be dropped after the test is over Link: https://stackoverflow.com/questions/42135114/how-does-spring-jpa-hibernate-ddl-auto-property-exactly-work-in-spring  
}) //This is done to only load the neccesary context for testing the repository + creating a in-memory database for testing purposes + ensuring test are run in transactions
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository roleRepository;

    private Role testRole;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        testRole = new Role();
        testRole.setName("password");
        roleRepository.save(testRole);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        roleRepository.delete(testRole);
    }

    @Test
    /**
     * This method tests that the setup is able to correctly save a role entity to the database and that this can be retrived using findById
     */
    void testSaveAndFindById()
    {
        Role role = roleRepository.findById(testRole.getRoleId()).orElse(null);
        assertNotNull(role);
        assertEquals(testRole.getName(), role.getName());
    }

    @Test
    /**
     * This tests the findByName method
     */
    void findByNameTest()
    {
        //We first test that the method can find one with a name
        Role role = roleRepository.findByName("password");
        assertNotNull(role);
        assertEquals(testRole.getName(), role.getName());
        //And then that it will not find anything for a name that does not exist
        role = roleRepository.findByName("nonExistentName");
        assertNull(role);
    }

    @Test
    /**
     * This tests the constrains for the role entity
     */
    void roleConstraintTest()
    {

        testRole = new Role();
        testRole.setName(null);    
        var e = assertThrowsExactly(ConstraintViolationException.class, () -> roleRepository.saveAndFlush(testRole));
        System.out.println(e.getMessage());
        assertEquals(true,e.getConstraintViolations().toString()
        .contains("ConstraintViolationImpl{interpolatedMessage='Each role must have descriptor!', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Role, messageTemplate='Each role must have descriptor!'}")
        &&
        e.getConstraintViolations().toString()
        .contains("ConstraintViolationImpl{interpolatedMessage='Each role must have non-null descriptor!', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Role, messageTemplate='Each role must have non-null descriptor!'}")
        );

        testRole = new Role();
        testRole.setName("");    
        e = assertThrowsExactly(ConstraintViolationException.class, () -> roleRepository.saveAndFlush(testRole));
        assertEquals("[ConstraintViolationImpl{interpolatedMessage='Each role must have descriptor!', propertyPath=name, rootBeanClass=class com.example.demo.domain.entity.Role, messageTemplate='Each role must have descriptor!'}]",e.getConstraintViolations().toString());

    }
}
