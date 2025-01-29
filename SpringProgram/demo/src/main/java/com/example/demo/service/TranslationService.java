package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.domain.dto.CompetenceDTO;
import com.example.demo.domain.entity.Competence;
import com.example.demo.presentation.restException.SpecificCompetenceNotFound;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.RepositoryExample;

@Service
//This service is resonsible for handling translation related logic
public class TranslationService {
    private final CompetenceRepository competenceRepository;


    /**
     * Constructs a new instance of the TranslationService (Spring boot managed).
     *
     * @param competenceRepository the repository for accessing competence database data
     */
    public TranslationService(CompetenceRepository competenceRepository) {
        this.competenceRepository = competenceRepository;
    }

    /**
     * Returns a list of all the existing competences, may contain 0 or more elements
     * @return the list containing the existing competences
     * 
     */
    public List<? extends CompetenceDTO> getCompetences()
    {
        return competenceRepository.findAll();
    }

    /**
     * 
     * @return 
     * 
     */
    public CompetenceDTO getSpecificCompetence(Integer id)
    {
        Optional<Competence> competenceContainer=competenceRepository.findById(id);

        if (competenceContainer.isPresent() ==false ) {
            throw new SpecificCompetenceNotFound(id);
        }

        return competenceContainer.get();
    }
}
