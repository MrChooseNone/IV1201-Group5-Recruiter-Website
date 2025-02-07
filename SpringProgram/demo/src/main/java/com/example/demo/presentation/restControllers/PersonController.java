package com.example.demo.presentation.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Person;
import com.example.demo.repository.PersonRepository;
import com.example.demo.service.PersonService;

@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController {
    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService){
        this.personService = personService;
    }

    @PostMapping("/add")
    public String addPerson(@RequestParam String name, @RequestParam String surname){
        personService.AddPerson(name, surname);
        return "Person added: " + name + " " + surname;
    }

    @GetMapping("/find")
    public List<? extends PersonDTO> findPersonByName(@RequestParam String name){
        return personService.FindPeopleByName(name);
    }
}
