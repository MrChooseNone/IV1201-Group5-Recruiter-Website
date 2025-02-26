package com.example.demo.domain.entity;

import com.example.demo.domain.dto.CompetenceTranslationDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a competence translation entity in the system.
 * Implements the CompetenceTranslationDTO interface.
 */
@Entity
public class CompetenceTranslation implements CompetenceTranslationDTO{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "competence_translation_id")
    private Integer competenceTranslationId;

    @JoinColumn(name = "competence_id", referencedColumnName = "competence_id")
    @ManyToOne
    @NotNull(message = "Each translation must be for a specific competence")
    private Competence competence;

    @JoinColumn(name = "language_id", referencedColumnName = "language_id")
    @ManyToOne
    @NotNull(message = "Each translation must be for a specific language")
    private Language language;

    @Column(name="translation")
    @NotNull(message = "Each translation must have a non-null translation")
    @NotBlank(message = "Each translation must have an actual translation")
    private String translation;

    /**
     * Implements the CompetenceTranslationDTO function getCompetenceTranslationId, and returns the competence translation's id
     * @return the competence translation's id
     */
    @Override
    public Integer getCompetenceTranslationId() {
        return this.competenceTranslationId;
    }

    /**
     * Implements the CompetenceTranslationDTO function getCompetence, and returns the competence this translation is for
     * @return the competence this is a translation for
     */
    @Override
    public Competence getCompetence() {
        return this.competence;
    }

    /**
     * Implements the CompetenceTranslationDTO function getLanguage, and returns the language this competence translation is for
     * @return the language this translation is for
     */
    @Override
    public Language getLanguage() {
        return this.language;
    }

    /**
     * Implements the CompetenceTranslationDTO function getTranslation, and returns the translation
     * @return the translation
     */
    @Override
    public String getTranslation() {
        return this.translation;
    }
    
    /**
     * Setter for setting a new competence
     * @param competence the competence this translation is for
     */
    public void SetCompetence(Competence competence) {
        this.competence=competence;
    }

    /**
     * Setter for setting a new language
     * @param language the language this translation is for
     */
    public void SetLanguage(Language language) {
        this.language=language;
    }

    /**
     * Setter for setting a new translation
     * @param translation the translation itself
     */
    public void SetTranslation(String translation) {
        this.translation=translation;
    }
}
