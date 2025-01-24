package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.entity.entityExample;


//interface is responsible for providing CRUD operations for the entityExample entity.
@Repository
public interface RepositoryExample extends JpaRepository<entityExample,Integer>{
    
}
