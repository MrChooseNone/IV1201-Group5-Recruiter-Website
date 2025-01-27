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

        //We then load the competences
        if (competenceRepository.findByName("ticket sales")==null) {
            Competence competence = new Competence();
            competence.setId(1);
            competence.setName("ticket sales");
            competenceRepository.save(competence);
        }
        if (competenceRepository.findByName("lotteries")==null) {
            Competence competence = new Competence();
            competence.setId(2);
            competence.setName("lotteries");
            competenceRepository.save(competence);
        }
        if (competenceRepository.findByName("roller coaster operation")==null) {
            Competence competence = new Competence();
            competence.setId(3);
            competence.setName("roller coaster operation");
            competenceRepository.save(competence);
        }

    }
    
    //This function is responsible for loading availability periods into the database
    private void loadAvailabilityPeriods()
    {

    }

    //This function is responsible for loading availability periods into the database
    private void loadCompetenceProfile()
    {

    }

    //This functions is responsible for loading people into the database
    private void loadPerson()
    {

    }
}
