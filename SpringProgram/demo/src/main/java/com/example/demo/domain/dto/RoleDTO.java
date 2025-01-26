package com.example.demo.domain.dto;

/* 
 * The RoleDTO interface provides a data transfer interface for objects representing a role.
*/
public interface RoleDTO {
    /** Interface function for retriving a role's role id
     * 
     * @return the role's role id
     */
    public Integer GetRoleId();

    /** Interface function for retriving a role's name
     * 
     * @return the role's first name
     */
    public String GetName();
}
