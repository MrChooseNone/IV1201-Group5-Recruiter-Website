package com.example.demo.config;


import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.CompetenceTranslationRepository;
import com.example.demo.repository.LanguageRepository;

@Component
//This class is responsible for loading data into the database
public class DatabaseLoader implements CommandLineRunner{

    //We define the repositories we will access
    private final LanguageRepository languageRepository;
    private final CompetenceTranslationRepository competenceTranslationRepository;
    private final CompetenceRepository competenceRepository;

    //This ensures Spring creates and loads the specified repositories
    public DatabaseLoader(LanguageRepository languageRepository,CompetenceTranslationRepository competenceTranslationRepository, CompetenceRepository competenceRepository) {
    this.languageRepository=languageRepository;
    this.competenceTranslationRepository=competenceTranslationRepository;
    this.competenceRepository=competenceRepository;
    }

    //This overwritten method will be run before the server starts, and is responsible for loading in data
    @Override
    public void run(String... args) throws Exception {
        //This loader is reponsible for loading in data not yet added to the sql script, or for tables which are created by JPA, and should be run AFTER running the sql script, otherwise it will fail! 
        
        //We load a language, and then a translations for that language
        Language language=null;
        if (languageRepository.findByName("english") == null) {
            System.out.println("Adding english");
            language = new Language();
            language.SetLanguageName("english");
            languageRepository.save(language);
        }
        else
        {
            System.out.println("english already exists");
            language=languageRepository.findByName("english");
        }


        //If no translations for this language is loaded, load a few
        List<CompetenceTranslation> englishTranslations=competenceTranslationRepository.findByLanguage_id(language.getLanguageId());
        if (englishTranslations==null||englishTranslations.size()==0) {
            System.out.println("Adding english translations");
            CompetenceTranslation translation = new CompetenceTranslation();
            translation.SetCompetence(competenceRepository.findById(1).get());
            translation.SetLanguage(language);
            translation.SetTranslation("Translation 1");
            competenceTranslationRepository.save(translation);
            translation = new CompetenceTranslation();
            translation.SetCompetence(competenceRepository.findById(2).get());
            translation.SetLanguage(language);
            translation.SetTranslation("Translation 2");
            competenceTranslationRepository.save(translation);
            translation = new CompetenceTranslation();
            translation.SetCompetence(competenceRepository.findById(3).get());
            translation.SetLanguage(language);
            translation.SetTranslation("Translation 3");
            competenceTranslationRepository.save(translation);
        }
        else
        {
            System.out.println("english translations already exist, specifically there exist " + englishTranslations.size() +" english translations");
        }

        
    }
    
}
