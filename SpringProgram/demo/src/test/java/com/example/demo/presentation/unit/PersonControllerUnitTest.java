package com.example.demo.presentation.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.domain.dto.PersonDTO;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restControllers.PersonController;
import com.example.demo.service.PersonService;

@ExtendWith(MockitoExtension.class)
public class PersonControllerUnitTest {
    // This is used to define the service we want to mock
    @Mock
    private PersonService personService;

    // We then define the controller we want to test, and state we want to inject
    // the above defined mock object instead of the real one
    @InjectMocks
    private PersonController personController;

    // This is used to mock the service actually returning something relevant
    static List<Person> savedPerson = new ArrayList<Person>();

    // This ensures mocks are created correctly
    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(PersonControllerUnitTest.class);
    }

    //This ensures the mock "databases" are clean after each attempt
    @AfterEach
    public void afterEach()
    {
        savedPerson.clear();
    }

    @Test
    /**
     * This is a test for the addPerson method
     * TODO update this when the controller is updated
     */
    void addPersonTest() {
        // Note we do not mock the personService, this is since the addPerson method
        // does not return anything
        // TODO add mock if it is changed to return anything
        //String result = personController.addPerson("name", "surname","password");
        //assertEquals("Person added: name surname", result);
    }

    @Test
    /**
     * This is a test for the findPersonByName method
     * TODO update this when the controller is updated
     */
    void findPersonByNameTest() {
        // First we define the test object

        Person person = new Person();
        person.setName("test");

        // We then define the mock implementation for the service function
        when(personService.FindPeopleByName(anyString())).thenAnswer(invocation -> {
            List<Person> returnList = new ArrayList<Person>();
            String stringArg = (String) invocation.getArguments()[0];
            for (Person p : savedPerson) {
                if (p.getName() == stringArg) {
                    returnList.add(p);
                }
            }
            return returnList;
        });

        // Then we test the function, first with service returning an empty list
        List<? extends PersonDTO> result = personController.findPersonByName("test");
        assertEquals(0, result.size());

        // We verify that the mock service was used as expected
        Mockito.verify(this.personService, Mockito.times(1)).FindPeopleByName(anyString());

        // We then test that if the service has a match it will return it
        savedPerson.add(person);
        result = personController.findPersonByName("test");
        assertEquals(1, result.size());
        assertEquals(person, result.get(0));

        // We verify that the mock service was used as expected
        Mockito.verify(this.personService, Mockito.times(2)).FindPeopleByName(anyString());

        // And finally we confirm that it passes the string parameter correctly (aka
        // still 0 for another name)
        result = personController.findPersonByName("name");
        assertEquals(0, result.size());

        // We verify that the mock service was used as expected
        Mockito.verify(this.personService, Mockito.times(3)).FindPeopleByName(anyString());

    }
}
