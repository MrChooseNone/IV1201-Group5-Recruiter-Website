package com.example.demo.domain.dto;


/** 
 * The CompetenceTranslationDTO interface provides a data transfer interface for handling competence translations
*/
public interface CompetenceTranslationDTO {
    /** Interface function for retriving a competence translation's id
     * 
     * @return competence competence translation's id
     */
    public Integer getCompetenceTranslationId();

    /** Interface function for retriving the competence this translation is for
     * 
     * @return the competence the translation is for
     */
    public CompetenceDTO getCompetence();

    /** Interface function for retriving the language this translation is for
     * 
     * @return the competence the competence profile explains
     */
    public LanguageDTO getLanguage();

    /** Interface function for retriving the translation itself
     * 
     * @return the translation
     */
    public String getTranslation();
}
