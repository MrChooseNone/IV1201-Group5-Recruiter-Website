package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource("SpringProgram\\demo\\src\\test\\java\\com\\example\\demo\\resources\\application-test.properties") 
//This is used to specify the property file : https://docs.spring.io/spring-framework/reference/testing/annotations/integration-spring/annotation-testpropertysource.html

@ActiveProfiles("test") //This is done for the purpose of loading the application-test properties file
//Based on stack overflow: https://stackoverflow.com/questions/73405438/how-to-get-properties-from-env-file-in-spring-boot-test
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
