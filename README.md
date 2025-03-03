# A Theme Park Recruitment System

This project is a recruitment system for a hypothetical theme park. It uses PostgreSQL as the database system, a Springboot server as the backend and a React-based frontend, and it follows the Monolithic Architectural pattern.

It was created as part of the course IV1201 "Arkitektur och design av globala applikationer" during the spring term 2025.

This project was to contributed to by the following people:

1. Erik Paulinder also known as Hustra03
2. Valter Lindberg also known as ValterLindberg
3. Alexander Ohlsson also known as MrChooseNone
4. Samuel Lövkvist, also known as Samuel Lövkvist

Cloud hosting link: (Add when this is added!)

## Project Structure

Below are more detailed descriptions of the purpose of the different parts of the project, with a focus on the structure and general relationships, but it will not fully detail all of the existing files and their purpose, since that is something which is constantly developed.

### General aspects

This section will discuss the general files included, but which do not belong to any category and which are most important.

- .env: This file needs to be created by any who wish to run it locally, as specified in [General Instructions](#General-instructions)

### Database

The only file of note here is the existing-database.sql, which is the sql file which is used to populate the database with the existing data.

### Backend

Below is a description of the backend systems structure.

Since this is a maven project, it includes a pom.xml file to define dependencies, along with a few mvnw files to help define the build process.

#### System

The actual backend system itself is defined by the files in SpringProgram\demo\src\main

- Resources contains the application.properties file, which defines many attributes for the spring application, including the database setting (some of which are imported from the system environment, to which the .env file is exported). Its subfolder logs is also where the application logs are stored by default, even if this can be modified in the application.properties folder.

- The "actual" project files are stored in the java\com\example\demo folder, with each subfolder representing a specific part of the system. The DemoApplication.java is the file used to start the Spring Boot application.
  - The config folder contains the classes used to define configuration for different aspects, for example WebConfig.java is used to configure Cross Origin requests for a specific url, which is done to allow the frontend system access to the backend.
  - The domain folder contains some subfolders, but in addition to these there is the ApplicationStatus.java class, which represents an enum for the application status, which can have the values 'accepted', 'denied' and 'unchecked'.
    - The subfolder dto contains the files used to define the dto's, which are interfaces used to define the standard functions any object representing that type of entity should implement, and is used to ensure the presentation layer is unaware of the implementation details, thus allowing it to be modified, which is part of the separation of concerns between the layers.
    - The entity subfolder contains files which are used to define the actual implementation for the system's entities, with each of these implementing the corresponding dto. These are what is actually used in the lower layers (service + integration) to represent the relevant data.
    - The requestBodies subfolder contains the files which define Spring request bodies. In Spring, a request body is a class defined to allow Spring to parse bodies from requests into a specific format. For example, ApplicationSubmissionRequestBody.java defines that the applicationEndpointController endpoint /submit should receive a body containing an integer, and two list of integers.
  - The filter folder contains files which define filters, which are a type of component used to perform some action on requests before they reach the requested endpoint. For example, JwtAuthFilter.java defines a Spring component that handles verifying that a request holds a valid token (!fact check this).
  - The presentation folder contains 3 subfolders
    - restAdvice contains Spring RestControllerAdvice classes, which contain methods which are called whenever an exception is thrown, and is used to send a response with the correct error message and http status code.
    - restControllers is where the classes defining the endpoints are located, and these are the first code Spring calls once it has parsed the http request.
    - restException contains the custom defined exceptions, which are used to give the correct http response code.
  - The repository folder is where the classes which access the database are stored. These use JPARepository to handle the implementation, with standard functions augmented by "custom" functions which follow JPA's query generation format.
  - The service folder defines the classes which handle business logic, with these generally being called by the controllers, and being responsible for parsing any business logic and calling the repository classes to retrieve information from the database.

#### Tests

The tests for the backend system generally follow one of three formats, with these using different types of techniques which come with advantages and disadvantages. All of the tests use jUnit, since that is a good unit testing library for Java.

The repository functions are tested by the tests in the repository folder, with these using h2 to create an in-memory database, and then testing both some of JPA's standard functions, along with any custom defined functions. This is done to avoid the tests interacting with the real database, since the created tests are unit tests, and thus their focus is testing that the class itself functions itself.

The service and the presentation layer tests in the unit subfolder all are tested by creating fake instances of the objects dependencies, using Mockito, and then defining implementations for these fake functions manually. This is done to avoid the dependencies implementation impacting the unit test. Otherwise the tests are relatively standard, testing that the function both throws the correct exceptions, and returns the correct values, depending on the input.

The presentation layer tests in the integration subfolder however does not use Mockito, and instead MockMVC is used. MockMVC is used to create a less extensive Spring context, while still allowing requests to be parsed as if they were received for real, and this is used to test certain aspects which can not be tested by treating the controllers as standard Java objects. One example of this are any endpoints with optional parameters, since this is something Spring handles before calling the endpoint controller, therefore just testing the class itself would not capture this. However, so far no way to mock dependencies for MockMVC has been found, with any examples found using deprecated annotations, therefore these tests depend on the actual database, and function more like integration tests rather than unit tests.

### Frontend

The frontend contains 2 notable subfolders, public and src.

- Public is where more static resources are stored, for example the website icon is stored here.
- Src is where the frontend code itself is stored, and it in turn contains two notable folders, along with quite a few relevant files, such as App.js which defines the routing, or the .css files which define the styling of the website.
  - App.js is the file which defines the routing of the system
  - components contain the different components used in the frontend. Each component serves a specific self-contained purpose, for example AppBar is the header, and these are fully reusable.
  - pages defines the structure for the different sections of the website, and uses the above components to create each of the pages.

## Setup instructions

Below are the instructions for the steps to take to set up the project to run locally, starting with the general instructions but also including a specific example of how to run the project correctly in visual studio code.

### General instructions

To run this project locally, you need to ensure you have the following installed:

1. Npm should be installable locally, since that is used in the frontend to handle dependencies.
2. Java SDK, the backend uses java version 17, but generally later versions should also work, perhaps with minor revisions.
3. Maven, the backend uses this tool to handle dependencies and building, so it is needed.
4. PostgreSQL, this is the database system utilized for this project, so any attempt to run it locally requires that such a database is also available for it to function. Note that other database systems can be utilized, as long as they are supported by JPA, but that some minor changes to the application.properties file would need to be made, and the systems functionality may be compromised.

If you want to run the tests, it is recommended to use some IDE with testing capability, such as Visual Studio Code, and install, if necessary, any relevant extensions, which for VScode is just Microsoft's "Extension Pack for Java".

Before any of the project's systems are run, there are a few steps which should be taken.

1. Create a database in postgres, the names does not matter but it will be used in a later step
2. Run the existing-database.sql file in the newly created database, this is done to populate it with the existing data, since the SprintBoot program will create any missing tables, but it will not populate it with all the needed data.
3. Create an .env file at the top of the project structure, it should be on the same level as this README.
4. Write the following lines into the .env file, with the correct values being used to substitute the parenthesis.
   - JDBC_DATABASE_URL= This should be a url to the newly created database to use, for example jdbc:postgresql://localhost:5432/testdatabase
   - JDBC_DATABASE_USERNAME= This should be the username to use in the database, for example postgres
   - JDBC_DATABASE_PASSWORD= This should be the password to use, for example "1234" if the password is 1234
   - ALLOWED_ORIGINS = This should be the url for the frontend that connects to the backend, for example http://localhost:3000
5. Create another .env file in the frontend folder
6. Write the following line into the .env file
    - REACT_APP_API_URL= This should be the url for the backend that the frontend sends requests to, for example http://localhost:8080
7. Run `npm install` in the frontend folder, it should be installed directly in the folder.

It should now be possible to run the frontend and backend systems locally, with the frontend being started by running `npm start` in the frontend folder, however the backend needs to be run with a few parameters, with the exact method used depending on how you wish to run the Spring Boot program.

To be more specific, we need to import the values from the .env file into the system environment, which results in execution looking, something like the following:

`${env:JDBC_DATABASE_URL}='jdbc:postgresql://localhost:5432/testdatabase'; ${env:JDBC_DATABASE_USERNAME}='postgres'; ${env:JDBC_DATABASE_PASSWORD}='1234'; ${env:jwt_secret}='ukopQPur+a8DXUypzUuLjPy1IrYKqRsaWBa0F0TxgehMcrfaXbgKLj5VoiSR97qG+5uAXIW8BpRjcp7hFUbZEg=='; ${env:CROSS_ORIGIN}='http://localhost:5173'; & 'C:\Program Files\Eclipse Adoptium\jdk-21.0.6.7-hotspot\bin\java.exe' '@C:\Users\carin\AppData\Local\Temp\cp_cmra2d8z0wr7ga0go4bi06mw.argfile' 'com.example.demo.DemoApplication'`

Exactly how you specify the environmental variables may depend on which java compiler you use, so below is a description of how to set up visual studio code to run the file in the following way "automatically".

### Visual studio code execution instructions

In visual studio code how it executed can be specified by creating a launch.json file in the .vscode folder, in this case we want the following content:

```
{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "DemoApplication",
            "request": "launch",
            "mainClass": "com.example.demo.DemoApplication",
            "projectName": "demo",
            "envFile": "${workspaceFolder}/.env"
        }
    ]
}
```

Note that the most important line is the envFile parameter, since that is what tells the IDE to automatically import the environmental variables in a correct manner.

## Future development plans and suggestions

Below are suggestions regarding how to implement the future requested features:

- Decision making component: The most sensible way to implement this would likely be to, upon the submission of an application, send it to the decision making system for analysis, and then storing this analysis with the application itself. Reviewers could then easily sort by these parameters, which would avoid this having to be re-done whenever a reviewer wants an analysis of an application.
- Mobile app: This could be developed relatively easily, since the vast majority of the systems logic currently rests in the backend system, and the backend could be used for the mobile app as easily as the frontend uses it. There may be some modifications which would need to be made, to account for different design requirements, but otherwise no major changes should be necessary.
