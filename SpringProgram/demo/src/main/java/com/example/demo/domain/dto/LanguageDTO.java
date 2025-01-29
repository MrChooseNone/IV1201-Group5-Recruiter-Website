package com.example.demo.domain.dto;

/**
 * The LanguageDTO interface provides a data transfer interface for handling languages
 */
public interface LanguageDTO {
    /** Interface function for retriving a language's id
     * 
     * @return the language's id
     */
    public Integer getLanguageId();

    /** Interface function for retriving a language's name
     * 
     * @return the language's name
     */
    public String getLanguageName();
}
