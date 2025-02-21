package com.example.demo.domain.requestBodies;

/**
 * This class defines the request body for the endpoint /person/register.
 * Below is an example of a valid JSON body which could be parsed into this class:
 * {
 *   "name": "John",
 *   "surname": "Doe",
 *   "pnr": "12345678-1234",
 *   "email": "john.doe@example.com",
 *   "password": "securepassword",
 *   "username": "johndoe"
 * }
 */
public class PersonRegistrationRequestBody {
    private String name;
    private String surname;
    private String pnr;
    private String email;
    private String password;
    private String username;

    /**
     * Default constructor.
     */
    public PersonRegistrationRequestBody() {}

    /**
     * Complete constructor.
     */
    public PersonRegistrationRequestBody(String name, String surname, String pnr, String email, String password, String username) {
        this.name = name;
        this.surname = surname;
        this.pnr = pnr;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    /**
     * This is a getter for a person's name
     * @return this instances person's name
     */
    public String getName() {
        return name;
    }

    /**
     * This is a getter for a person's surname
     * @return this instances person's surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * This is a getter for a person's personal identity number
     * @return this instances person's personal identity number
     */
    public String getPnr() {
        return pnr;
    }

    /**
     * This is a getter for a person's email
     * @return this instances person's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * This is a getter for a person's password
     * @return this instances person's password
     */
    public String getPassword() {
        return password;
    }

    /**
     * This is a getter for a person's username
     * @return this instances person's usernname
     */
    public String getUsername() {
        return username;
    }
}
