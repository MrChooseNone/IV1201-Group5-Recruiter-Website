package com.example.demo.presentation.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.dto.CompetenceDTO;
import com.example.demo.service.TranslationService;

@RestController
@RequestMapping("/translation")
/**
 * This endpoint controller is responsible for handeling the requests concerning
 * translation.
 * This includes, for example, handeling finding the competence translations for
 * a specific language
 */
public class TranslationEndpointController {
    @Autowired
    private final TranslationService translationService;

    /**
     * Constructs a new instance of the TranslationEndpointController (this is
     * Spring boot managed).
     * 
     * @param translationService The service used to handle translation related
     *                           manners.
     */
    public TranslationEndpointController(TranslationService translationService) {
        this.translationService = translationService;
    }

    /**
     * This function returns the existing standard competences
     * 
     * @return This function will return the list of existing competences as a json
     *         object to the user
     */
    @GetMapping("/getStandardCompetences")
    public List<? extends CompetenceDTO> standardCompetences() {

        List<? extends CompetenceDTO> existingCompetences = translationService.getCompetences();
        System.out.println(existingCompetences);
        return existingCompetences;
    }

    /**
     * 
     */
    @GetMapping("/getSpecificCompetence/{id}")
    public CompetenceDTO GetSpecificCompetence(@PathVariable Integer id) {

        return translationService.getSpecificCompetence(id);
    }

    /**
     * 
     */
    @GetMapping("/getCompetenceTranslation")
    public String GetCompetenceTranslation(@RequestParam String language) {
        return "test123";
    }
}
