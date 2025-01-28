package com.example.demo.config;


import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.Role;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.RoleRepository;

@Component
//This class is responsible for loading data into the database
public class DatabaseLoader implements CommandLineRunner{

    //We define the repositories we will access
    private final RoleRepository roleRepository;
    private final CompetenceRepository competenceRepository;

    //This ensures Spring creates and loads the specified repositories
    public DatabaseLoader(RoleRepository roleRepository, CompetenceRepository competenceRepository) {
    this.roleRepository = roleRepository;
    this.competenceRepository=competenceRepository;
    }

    //This overwritten method will be run before the server starts, and is responsible for loading in data
    @Override
    public void run(String... args) throws Exception {

    }
    
}
