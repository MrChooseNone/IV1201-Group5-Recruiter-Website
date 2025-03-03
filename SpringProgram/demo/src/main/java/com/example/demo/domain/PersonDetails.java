package com.example.demo.domain;
import com.example.demo.domain.entity.Person;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PersonDetails implements UserDetails{
    private final Person person;

    public PersonDetails(Person person){
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority(person.getRole().getName()));
    }

    @Override
    public String getUsername(){
        return person.getName();
    }

    @Override
    public String getPassword(){
        return person.getPassword();
    }
    
}
