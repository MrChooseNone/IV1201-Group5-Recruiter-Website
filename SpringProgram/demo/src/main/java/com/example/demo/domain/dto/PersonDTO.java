package com.example.demo.domain.dto;

/** 
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

    /** Interface function for setting a person's first name
     * 
     * set the user's first name
     */
    public void setName(String name);

    /** Interface function for retriving a person's last name
     * 
     * @return the user's last name
     */
    public String getSurname();

    /** Interface function for setting a person's surname
     * 
     * @param surname set the user's surname
     */
    public void setSurname(String surname);

    /** Interface function for retriving a person's personal identity number
     * 
     * @return the user's personal identity number
     */
    public String getPnr();

    /** Interface function for setting a person's personal identity number
     * 
     * @param pnr set the user's personal identity number
     */
    public void setPnr(String pnr);

    /** Interface function for retriving a person's email
     * 
     * @return the user's email
     */
    public String getEmail();

    /** Interface function for setting a person's email
     * 
     * @param email set the user's email
     */
    public void setEmail(String email);

    /** Interface function for retriving a person's password
     * 
     * @return the user's password
     */
    public String getPassword();

    /** Interface function for setting a person's password
     * 
     * @param password set the user's password
     */
    public void setPassword(String password);

    /** Interface function for retriving a person's role
     * 
     * @return the users role
     */
    public RoleDTO getRole();

    /** Interface function for retriving a person's username
     * 
     * @return the users username
     */
    public String getUsername();

    /** Interface function for setting a person's username
     * 
     * @param username set the user's username
     */
    public void setUsername(String username);

}
