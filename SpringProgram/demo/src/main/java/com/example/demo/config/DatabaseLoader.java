package com.example.demo.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Role;
import com.example.demo.repository.RoleRepository;

@Component
//This class is responsible for loading data into the database
public class DatabaseLoader implements CommandLineRunner{

    //We define the repositories we will access
    private final RoleRepository roleRepository;

    //This ensures Spring creates and loads the specified repositories
    public DatabaseLoader(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
    }

    //This overwritten method will be run before the server starts, and is responsible for loading in data
    @Override
    public void run(String... args) throws Exception {
        //We check if the roles exist, and if not we create them
        if (roleRepository.findByName("recruiter") == null) {
            Role role = new Role();
            role.setId(1);
            role.setName("recruiter");
            roleRepository.save(role);
        }
        if (roleRepository.findByName("applicant") == null) {
            Role role = new Role();
            role.setId(2);
            role.setName("applicant");
            roleRepository.save(role);
        }
    }
    
    
}
