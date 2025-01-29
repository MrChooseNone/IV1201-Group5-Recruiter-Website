package com.example.demo.domain.dto;

/* 
 * The PersonDTO interface provides a data transfer interface for objects representing a person.
*/
public interface PersonDTO {
    
    /** Interface function for retriving a person's id
     * 
     * @return the user's first name
     */
    public Integer getId();

    /** Interface function for retriving a person's first name
     * 
     * @return the user's first name
     */
    public String getName();

    /** Interface function for retriving a person's last name
     * 
     * @return the user's last name
     */
    public String getSurname();

    /** Interface function for retriving a person's personal identity number
     * 
     * @return the user's personal identity number
     */
    public String getPnr();

    /** Interface function for retriving a person's email
     * 
     * @return the user's email
     */
    public String getEmail();

    /** Interface function for retriving a person's password
     * 
     * @return the user's password
     */
    public String getPassword();

    /** Interface function for retriving a person's role
     * 
     * @return the users role
     */
    public RoleDTO getRole();

}
