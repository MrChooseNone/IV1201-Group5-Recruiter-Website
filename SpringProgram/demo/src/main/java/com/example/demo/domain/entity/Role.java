package com.example.demo.domain.entity;

import com.example.demo.domain.dto.RoleDTO;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
/**
 * Represents a role entity in the system.
 * Implements the RoleDTO interface.
 */
public class Role implements RoleDTO{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name="role_id")
    private Integer roleId;

    @Column(name="name")
    @NotNull(message = "Each role must have non-null descriptor!")
    @NotBlank(message="Each role must have descriptor!")
    private String name;

    /**
     * Implements the RoleDTO function getRoleId, and returns the role objects role id
     * @return the role's role id
     */
    @Override
    public Integer getRoleId() {
        return this.roleId;
    }

    /**
     * Implements the RoleDTO function getName, and returns the role objects name
     * @return the role's name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set the role's id.
     * 
     * @param id the role id to set
     */
    public void setId(Integer id)
    {this.roleId=id;}

    /**
     * Set the role's name.
     * 
     * @param name the role name to set
     */
    public void setName(String name)
    {this.name=name;}
}
