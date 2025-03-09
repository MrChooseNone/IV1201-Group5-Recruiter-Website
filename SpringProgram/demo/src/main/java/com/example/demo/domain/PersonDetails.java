package com.example.demo.domain;
import com.example.demo.domain.entity.Person;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * PersonDetails is a custom implementation of UserDetails that is used for Spring Security authentication.
 * It provides the details of a Person entity for authentication purposes.
 */
public class PersonDetails implements UserDetails{
    private final Person person;

    /**
     * Constructs a PersonDetails object with the given Person entity.
     * @param person the Person entity to be used for user details
     */
    public PersonDetails(Person person){
        this.person = person;
    }

    /**
     * Returns the authorities granted to the user, based on the person's role.
     * @return a collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(person.getRole().getName()));
    }

    /**
     * Returns the username of the person.
     * @return the username
     */
    @Override
    public String getUsername(){
        return person.getUsername();
    }

    /**
     * Returns the password of the person.
     * @return the password
     */
    @Override
    public String getPassword(){
        return person.getPassword();
    }


    /**
     * This is a getter for the person id
     * @return The person id for the person this request is for
     */
    public Integer getPersonId(){
        return person.getId();
    }
    
}
