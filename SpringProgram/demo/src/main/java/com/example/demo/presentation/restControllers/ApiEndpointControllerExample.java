package com.example.demo.presentation.restControllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.presentation.restException.InvalidParameterException;
import com.example.demo.service.ServiceExample;

//This is an example of how a API endpoint should look for the REST controller
//The presention folder is where these are located, and they should validate the inputs are the correct format before calling the domain/integration layers for futher logic/database accesses

//We define this as a rest controller, and mark which endpoint it should handle
@RestController
@RequestMapping("/test")
public class ApiEndpointControllerExample {
    @Autowired
    private final ServiceExample exampleService;
    /**
     * Constructs a new instance of the ApiEndpointControllerExample
     * (Spring boot managed).
     *   @param exampleService The service used to show how this can be handeled, tells spring this controller requiers an instance of this service.
     */
    public ApiEndpointControllerExample(ServiceExample exampleService) {
        this.exampleService = exampleService;
    }

    /**
     * Sends a static test message to any user
     */
    @GetMapping("/")
    public String basicAnswer() {
        return "test123";
    }

    /**
     * Sends a static test message, which is created by communicating with a service
     */
    @GetMapping("/service")
    public String basicServiceAnswer() {
        return exampleService.basicTest();
    }

    /**
     * Sends a variable test message, which is created by communicating with a service and uses parameters to check if a id exists in the test database, provided using ?id={value}
     */
    @GetMapping("/database")
    public String basicDatabaseAnswer(@RequestParam String person_id) {
        //Note we need to parse "manually" for types like integer, otherwise we can not send custom error messages for incorrect values
        Integer convertedId;
        try {
           convertedId=Integer.parseInt(person_id);
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("input was not a valid integer");
        }

        return exampleService.findIfExistsById(convertedId) +"\n"+exampleService.findCount();
    }
}
