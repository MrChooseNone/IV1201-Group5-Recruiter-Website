package com.example.demo.domain.entity;

import com.example.demo.domain.dto.RoleDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Role implements RoleDTO{
    
    @Id
    @Column(name="role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleId;

    @Column(name="name")
    private String name;

    /**
     * Implements the RoleDTO function GetRoleId, and returns the role objects role id
     * @return the role's role id
     */
    @Override
    public Integer GetRoleId() {
        return this.roleId;
    }

    /**
     * Implements the RoleDTO function GetName, and returns the role objects name
     * @return the role's name
     */
    @Override
    public String GetName() {
        return this.name;
    }

}
