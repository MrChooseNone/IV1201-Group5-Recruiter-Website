package com.example.demo.domain.dto;

/* 
 * The PersonDTO interface provides a data transfer interface for handling competence
*/
public interface CompetenceDTO {
    /** Interface function for retriving a competence's id
     * 
     * @return the competence's id
     */
    public Integer GetCompetenceId();

    /** Interface function for retriving a competence's name
     * 
     * @return the competence's name
     */
    public String GetName();
}
