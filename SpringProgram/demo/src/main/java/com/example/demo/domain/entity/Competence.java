package com.example.demo.domain.entity;

import com.example.demo.domain.dto.CompetenceDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * Represents a competence entity in the system.
 */
@Entity
public class Competence implements CompetenceDTO{
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "competence_id")
    private Integer competenceId;

    @Column(name="name")
    private String name;

    /**
     * Implements the CompetenceDTO function GetCompetenceId, and returns the competence's id
     * @return the role's role id
     */
    @Override
    public Integer GetCompetenceId() {
        return this.competenceId;
    }

    /**
     * Implements the CompetenceDTO function GetName, and returns the competence's name
     * @return the role's role id
     */
    @Override
    public String GetName() {
        return this.name;
    }
}
