package com.example.demo.domain.dto;

/** 
 * The CompetenceDTO interface provides a data transfer interface for handling competence objects
*/
public interface CompetenceDTO {
    /** Interface function for retriving a competence's id
     * 
     * @return the competence's id
     */
    public Integer getCompetenceId();

    /** Interface function for retriving a competence's name
     * 
     * @return the competence's name
     */
    public String getName();
}
