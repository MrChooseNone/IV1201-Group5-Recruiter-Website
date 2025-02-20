package com.example.demo.presentation.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.domain.dto.CompetenceDTO;
import com.example.demo.domain.dto.CompetenceTranslationDTO;
import com.example.demo.domain.dto.LanguageDTO;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceTranslation;
import com.example.demo.domain.entity.Language;
import com.example.demo.presentation.restControllers.TranslationEndpointController;
import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.TranslationService;
@ExtendWith(MockitoExtension.class)
/**
 * This class tests the TranslationEndpointController class
 */
public class TranslationEndpointControllerUnitTest {
    // This is used to define the service we want to mock
    @Mock
    private TranslationService translationService;

    // We then define the controller we want to test, and state we want to inject
    // the above defined mock object instead of the real one
    @InjectMocks
    private TranslationEndpointController translationEndpointController;

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(TranslationEndpointControllerUnitTest.class);
    }

    @Test
    /**
     * This is a test for the GetStandardCompetences method
     */
    void GetStandardCompetencesTest()
    {
        // We then define the mock implementation for the service function
        when(translationService.GetCompetences()).thenAnswer(invocation -> {
            List<Competence> returnList = new ArrayList<Competence>();
            returnList.add(new Competence());
            returnList.add(new Competence());
            return returnList;
        });

        //We then test the method and confirm it returns the same list as it recived from the service
        List<? extends CompetenceDTO> result=translationEndpointController.GetStandardCompetences();
        assertEquals(2, result.size());
    }

    @Test
    /**
     * This is a test for the GetSpecificCompetence method
     */
    void GetSpecificCompetenceTest()
    {
        // We then define the mock implementation for the service function, in this case if a real id is given it returns a fake competence matching it
        when(translationService.GetSpecificCompetence(anyInt())).thenAnswer(invocation -> {
            Integer intArg=(Integer)invocation.getArguments()[0];
            Competence c=new Competence();
            c.setId(intArg);
            return c;
        });

        var e = assertThrowsExactly(InvalidParameterException.class, () -> translationEndpointController.GetSpecificCompetence("notAnInteger"));
        assertEquals("Invalid parameter : Provided value (notAnInteger) could not be parsed as a valid integer", e.getMessage());

        //We then test the method and confirm it returns the competence it gets from the service if integer is a correct parameter
        CompetenceDTO result=translationEndpointController.GetSpecificCompetence("1");
        assertNotNull(result);
        assertEquals(1, result.getCompetenceId());
    }

    @Test
    /**
     * This is a test for the GetCompetenceTranslation method
     */
    void GetCompetenceTranslationTest()
    {
        // We then define the mock implementation for the service function, in this case if a language string is given it returns a fake translation matching it
        when(translationService.GetCompetenceTranslation(anyString())).thenAnswer(invocation -> {
            String stringArg=(String)invocation.getArguments()[0];
            Language language=new Language();
            language.SetLanguageName(stringArg);
            CompetenceTranslation translation = new CompetenceTranslation();
            translation.SetLanguage(language);
            translation.SetTranslation("testTranslation");
            List<CompetenceTranslation> translations=new ArrayList<CompetenceTranslation>();
            translations.add(translation);
            return translations;
        });

        //We then test the method and confirm it returns the competence translation it gets from the service
        List<? extends CompetenceTranslationDTO> result=translationEndpointController.GetCompetenceTranslation("testLanguage");
        assertNotNull(result);
        assertEquals(1, result.size());
        assertNotEquals("testLanguage", result.get(0).getLanguage().getLanguageName()); //This is since the endpoint passes the parameter in lower case, so it should not return with any upper case letters
        assertEquals("testlanguage", result.get(0).getLanguage().getLanguageName());
    }

    @Test
    /** 
     * This is a test for the GetLanguages method
     */
    void GetLanguagesTest()
    {
        List<Language> languages= new ArrayList<Language>();
        languages.add(new Language());
        languages.add(new Language());

        // We then define the mock implementation for the service function, in this it returns a list of languages
        when(translationService.GetLanguages()).thenAnswer(invocation -> {
            return languages;
        });

        //We then test the method and confirm it returns the languages list it gets from the service
        List<? extends LanguageDTO> result=translationEndpointController.GetLanguages();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

}
