package com.example.demo.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;
import com.example.demo.domain.entity.Person;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.CompetenceTranslationRepository;
import com.example.demo.repository.LanguageRepository;
import com.example.demo.repository.PersonRepository;

@Component
//This class is responsible for loading data into the database
public class DatabaseLoader implements CommandLineRunner{

    //We define the repositories we will access
    private final LanguageRepository languageRepository;
    private final CompetenceTranslationRepository competenceTranslationRepository;
    private final CompetenceRepository competenceRepository;
    private final PersonRepository personRepository;

    //This ensures Spring creates and loads the specified repositories
    public DatabaseLoader(LanguageRepository languageRepository,CompetenceTranslationRepository competenceTranslationRepository, CompetenceRepository competenceRepository, PersonRepository personRepository) {
    this.languageRepository=languageRepository;
    this.competenceTranslationRepository=competenceTranslationRepository;
    this.competenceRepository=competenceRepository;
    this.personRepository=personRepository;
    }

    //This overwritten method will be run before the server starts, and is responsible for loading in data
    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void run(String... args) throws Exception {

        // Insert Person data for testing
        Person person = new Person();
        person.setName("Alexander");
        person.setSurname("Ohlsson");
        // Save person to the database
        personRepository.save(person);
        
        // Declare language as final so it's effectively final
        final Language language;
        if (languageRepository.findByName("english") == null) {
            System.out.println("Adding english");
            language = new Language();
            language.SetLanguageName("english");
            languageRepository.save(language);
        } else {
            System.out.println("english already exists");
            language = languageRepository.findByName("english");
        }

        // If no translations for this language are loaded, load a few
        List<CompetenceTranslation> englishTranslations = competenceTranslationRepository.findByLanguage_id(language.getLanguageId());
        if (englishTranslations == null || englishTranslations.size() == 0) {
            System.out.println("Adding english translations");

            // Handle Optional for each Competence entry to avoid NoSuchElementException
            competenceRepository.findById(1).ifPresent(competence -> {
                CompetenceTranslation translation = new CompetenceTranslation();
                translation.SetCompetence(competence);
                translation.SetLanguage(language);
                translation.SetTranslation("Translation 1");
                competenceTranslationRepository.save(translation);
            });

            competenceRepository.findById(2).ifPresent(competence -> {
                CompetenceTranslation translation = new CompetenceTranslation();
                translation.SetCompetence(competence);
                translation.SetLanguage(language);
                translation.SetTranslation("Translation 2");
                competenceTranslationRepository.save(translation);
            });

            competenceRepository.findById(3).ifPresent(competence -> {
                CompetenceTranslation translation = new CompetenceTranslation();
                translation.SetCompetence(competence);
                translation.SetLanguage(language);
                translation.SetTranslation("Translation 3");
                competenceTranslationRepository.save(translation);
            });
        } else {
            System.out.println("english translations already exist, specifically there exist " + englishTranslations.size() + " english translations");
        }
    }


        
    
    
}
