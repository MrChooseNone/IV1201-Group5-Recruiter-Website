package com.example.demo.domain;
import com.example.demo.domain.entity.Person;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is used to handle authentication, by implementing the spring security standard UserDetails class
 */
public class PersonDetails implements UserDetails{
    private final Person person;

    /**
     * This is a constructor for the PersonDetails class
     * @param person The person this detail class should describe
     */
    public PersonDetails(Person person){
        this.person = person;
    }

    /**
     * This returns a list of authorities, in this case consisting fully of the users role
     * @return The users authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(person.getRole().getName()));
    }

    /**
     * This is a getter for the persons username
     * @return The users username
     */
    @Override
    public String getUsername(){
        return person.getUsername();
    }

    /**
     * This is a getter for the persons password
     * @return The users password
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
