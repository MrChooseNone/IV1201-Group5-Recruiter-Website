package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.TransientDataAccessException;

import com.example.demo.domain.ApplicationStatus;
import com.example.demo.domain.dto.CompetenceDTO;
import com.example.demo.domain.dto.CompetenceTranslationDTO;
import com.example.demo.domain.dto.LanguageDTO;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.LanguageNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.SpecificCompetenceNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.TranslationsNotFoundException;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.CompetenceTranslationRepository;
import com.example.demo.repository.LanguageRepository;

/**
 * This class defined the unit tests for the TranslationService class
 */
@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {

    // We first define the repositories we will mock
    @Mock
    private CompetenceRepository competenceRepository;
    @Mock
    private LanguageRepository languageRepository;
    @Mock
    private CompetenceTranslationRepository competenceTranslationRepository;

    // We then define the service we will test, and that it should use the above
    // mock repositories instead of the real ones
    @InjectMocks
    private TranslationService translationService;

    // These lists are used to mock the database
    static List<Competence> savedCompetence = new ArrayList<Competence>();
    static List<Language> savedLanguages = new ArrayList<Language>();
    static List<CompetenceTranslation> savedCompetenceTranslations = new ArrayList<CompetenceTranslation>();

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(TranslationServiceTest.class);
    }

    //This ensures the mock "databases" are clean after each attempt
    @AfterEach
    public void afterEach()
    {
        savedCompetence.clear();
        savedLanguages.clear();
        savedCompetenceTranslations.clear();
    }

    @Test
    /**
     * This is a test for the GetCompetence method
     */
    void GetCompetencesTest() {

        /**
         * We create an example competence object
         */
        Competence competence = new Competence();
        competence.setName("CompetenceName");

        //We define the implementation for the mock repository
        when(competenceRepository.save(any(Competence.class))).thenAnswer(invocation -> {
            savedCompetence.add((Competence)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceRepository.findAll()).thenAnswer(invocation -> {
            return savedCompetence;
        });

        //We then call the method which is to be tested
        List<? extends CompetenceDTO> competenceRepositoryReturnValue = translationService.GetCompetences();

        //Here we confirm the result, aka that with an empty "database" we will get an empty list
        assertNotNull(competenceRepositoryReturnValue);
        assertEquals(0, competenceRepositoryReturnValue.size());

        //We verify that the mock repository was used as expected
        Mockito.verify(this.competenceRepository, Mockito.times(1)).findAll();

        //We then add a competence manually, and then re-call the exact same method as above
        competenceRepository.save(competence);
        competenceRepositoryReturnValue = translationService.GetCompetences();

        //Here we confirm the results now that one was added manually, aka it was added and is the entire database
        assertNotNull(competenceRepositoryReturnValue);
        assertEquals(1, competenceRepositoryReturnValue.size());
        assertEquals(competence, competenceRepositoryReturnValue.get(0));
        Mockito.verify(this.competenceRepository, Mockito.times(2)).findAll();

        //We then test that it handles database exceptions correctly
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(competenceRepository).findAll();

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> translationService.GetCompetences());
        assertEquals("Failed due to database error, please try again",e5.getMessage());
    }

    @Test
    /**
     * This is a test for the GetSpecificCompetence method
     */
    void GetSpecificCompetenceTest()
    {
        //We create example competences
        Competence competence = new Competence();
        competence.setName("CompetenceName");
        competence.setId(0);

        
        Competence competence2 = new Competence();
        competence2.setName("CompetenceName2");
        competence2.setId(1);

        //We create stubs for the mock repository
        when(competenceRepository.save(any(Competence.class))).thenAnswer(invocation -> {
            savedCompetence.add((Competence)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceRepository.findById(anyInt())).thenAnswer(invocation -> {
            Integer competenceIdArgument=(Integer)invocation.getArguments()[0];
            Optional<Competence> cContainer;
            //Link to page regarding Java optional: https://www.baeldung.com/java-optional 
            for (Competence c : savedCompetence) {
                if (c.getCompetenceId()==competenceIdArgument) {
                    cContainer=Optional.of(c);
                    return cContainer;
                }
            }
            cContainer=Optional.empty();
            return cContainer;
        });

        //This tests if the specific exception is thrown, which it should be 
        //Link for stackoverflow on how to assert exception thrown : https://stackoverflow.com/questions/40268446/junit-5-how-to-assert-an-exception-is-thrown 
        assertThrowsExactly(SpecificCompetenceNotFoundException.class, ()->translationService.GetSpecificCompetence(0));
        Mockito.verify(this.competenceRepository, Mockito.times(1)).findById(anyInt());

        //We manually add the repository
        competenceRepository.save(competence);

        //We then call the specific function again and test the results
        CompetenceDTO result=translationService.GetSpecificCompetence(0);
        assertEquals(competence.getCompetenceId(), result.getCompetenceId());
        assertEquals(competence.getName(), result.getName());
        Mockito.verify(this.competenceRepository, Mockito.times(2)).findById(anyInt());

        //We then test that it handles database exceptions correctly
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(competenceRepository).findById(anyInt());

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> translationService.GetSpecificCompetence(0));
        assertEquals("Failed due to database error, please try again",e5.getMessage());
    }


    @Test
    /**
     * This is a test for the GetCompetenceTranslation method
     */
    void GetCompetenceTranslationTest()
    {
        //We define the example objects
        Competence competence = new Competence();
        competence.setName("CompetenceName");
        competence.setId(0);

        Language language = new Language();
        language.SetLanguageName("testLanguage");
        language.SetLanguageId(0);

        CompetenceTranslation translation = new CompetenceTranslation();
        translation.SetCompetence(competence);
        translation.SetLanguage(language);
        translation.SetTranslation("test translation");

        //Here we create the relevant stubs for the repositories used 
        when(languageRepository.save(any(Language.class))).thenAnswer(invocation -> {
            savedLanguages.add((Language)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(languageRepository.findByName(anyString())).thenAnswer(invocation -> {
            String languageName=(String)invocation.getArguments()[0];
            for (Language l : savedLanguages) {
                if (l.getLanguageName()==languageName) {
                    return l;
                }
            }
            return null;
        });

        when(competenceTranslationRepository.save(any(CompetenceTranslation.class))).thenAnswer(invocation -> {
            savedCompetenceTranslations.add((CompetenceTranslation)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(competenceTranslationRepository.findByLanguage_id(anyInt())).thenAnswer(invocation -> {
            List<CompetenceTranslation> returnList=new ArrayList<CompetenceTranslation>();

            Integer languageId=(Integer)invocation.getArguments()[0];
            for (CompetenceTranslation c : savedCompetenceTranslations) {
                if (c.getLanguage().getLanguageId()==languageId) {
                    returnList.add(c);
                }
            }
            return returnList;
        });

        //This tests if the specific exception is thrown as expected
        assertThrowsExactly(LanguageNotFoundException.class, ()->translationService.GetCompetenceTranslation("nothing"));
        Mockito.verify(this.languageRepository, Mockito.times(1)).findByName(anyString());

        //We then add a test language to the "database" and confirm it was added
        languageRepository.save(language);
        assertNotNull(languageRepository.findByName(language.getLanguageName()));
        Mockito.verify(this.languageRepository, Mockito.times(2)).findByName(anyString());


        assertThrowsExactly(LanguageNotFoundException.class, ()->translationService.GetCompetenceTranslation("nothing"));
        Mockito.verify(this.languageRepository, Mockito.times(3)).findByName(anyString());
        assertThrowsExactly(TranslationsNotFoundException.class, ()->translationService.GetCompetenceTranslation(language.getLanguageName()));
        Mockito.verify(this.languageRepository, Mockito.times(4)).findByName(anyString());

        competenceTranslationRepository.save(translation);

        //Here we test the competence translation manually
        List<CompetenceTranslation> results=competenceTranslationRepository.findByLanguage_id(language.getLanguageId());
        assertNotNull(results);
        assertEquals(1,results.size());
        assertEquals(translation.getTranslation(),results.get(0).getTranslation());

        List<? extends CompetenceTranslationDTO> translations=translationService.GetCompetenceTranslation(language.getLanguageName());

        assertNotNull(translations);
        assertEquals(translation, translations.get(0));


        //We then test that it handles database exceptions correctly
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(competenceTranslationRepository).findByLanguage_id(anyInt());

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> translationService.GetCompetenceTranslation(language.getLanguageName()));
        assertEquals("Failed due to database error, please try again",e5.getMessage());        
        
        //We then test that it handles database exceptions correctly
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(languageRepository).findByName(anyString());

        e5 = assertThrowsExactly(CustomDatabaseException.class, () -> translationService.GetCompetenceTranslation(language.getLanguageName()));
        assertEquals("Failed due to database error, please try again",e5.getMessage());

    }

    @Test
    /**
     * This is a test for the GetLanguages method
     */
    void GetLanguagesTest()
    {
        //This is a test to ensure the "database" is clean before we start
        assertEquals(0, savedLanguages.size());
        /**
         * We create an example language object
         */
        Language language = new Language();
        language.SetLanguageName("CompetenceName");

        //We define the implementation for the mock repository
        when(languageRepository.save(any(Language.class))).thenAnswer(invocation -> {
            savedLanguages.add((Language)invocation.getArguments()[0]);
            return invocation.getArguments()[0];
        });

        when(languageRepository.findAll()).thenAnswer(invocation -> {
            return savedLanguages;
        });

        //We then call the method which is to be tested
        List<? extends LanguageDTO> returnValue = translationService.GetLanguages();

        //Here we confirm the result, aka that with an empty "database" we will get an empty list
        assertNotNull(returnValue);
        assertEquals(0, returnValue.size());

        //We verify that the mock repository was used as expected
        Mockito.verify(this.languageRepository, Mockito.times(1)).findAll();

        //We then add a langague manually, and then re-call the exact same method as above
        languageRepository.save(language);
        returnValue = translationService.GetLanguages();

        //Here we confirm the results now that one was added manually, aka it was added and is the entire database
        assertNotNull(returnValue);
        assertEquals(1, returnValue.size());
        assertEquals(language, returnValue.get(0));
        Mockito.verify(this.languageRepository, Mockito.times(2)).findAll();

        //We then test that it handles database exceptions correctly
        doThrow(new TransientDataAccessException("Oops! Something went wrong.") {}).when(languageRepository).findAll();

        var e5 = assertThrowsExactly(CustomDatabaseException.class, () -> translationService.GetLanguages());
        assertEquals("Failed due to database error, please try again",e5.getMessage());
    }
}
