package com.example.demo.presentation.restControllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.entity.Person;
import com.example.demo.repository.PersonRepository;

@RestController
@RequestMapping("/person")
@CrossOrigin
public class PersonController {
    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository){
        this.personRepository = personRepository;
    }

    @PostMapping("/add")
    public String addPerson(@RequestParam String name, @RequestParam String surname){
        Person person = new Person();
        person.setName(name);
        person.setSurname(surname);

        personRepository.save(person);
        return "Person added: " + name + " " + surname;
    }

    @PostMapping("/find")
    public List<Person> findPersonByName(@RequestParam String name){
        return personRepository.findByName(name);
    }
}
