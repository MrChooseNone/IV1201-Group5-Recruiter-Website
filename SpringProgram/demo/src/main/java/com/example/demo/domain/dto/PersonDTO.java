package com.example.demo.domain.dto;

/* 
 * The PersonDTO interface provides a data transfer interface for objects representing a person.
*/
public interface PersonDTO {
    
    /** Interface function for retriving a person's id
     * 
     * @return the user's first name
     */
    public Integer GetId();

    /** Interface function for retriving a person's first name
     * 
     * @return the user's first name
     */
    public String GetName();

    /** Interface function for retriving a person's last name
     * 
     * @return the user's last name
     */
    public String GetSurname();

    /** Interface function for retriving a person's personal identity number
     * 
     * @return the user's personal identity number
     */
    public String GetPnr();

    /** Interface function for retriving a person's email
     * 
     * @return the user's email
     */
    public String GetEmail();

    /** Interface function for retriving a person's password
     * 
     * @return the user's password
     */
    public String GetPassword();

    /** Interface function for retriving a person's role
     * 
     * @return the users role
     */
    public RoleDTO GetRole();

}
