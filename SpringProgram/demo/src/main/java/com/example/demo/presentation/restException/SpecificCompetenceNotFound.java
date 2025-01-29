package com.example.demo.presentation.restException;

public class SpecificCompetenceNotFound extends RuntimeException {

    public SpecificCompetenceNotFound(Integer id) {
      super("Could not find specific competence with id : " + id);
    }
  }