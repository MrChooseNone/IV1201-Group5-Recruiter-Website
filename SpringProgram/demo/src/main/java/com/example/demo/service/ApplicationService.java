package com.example.demo.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.dto.ApplicationDTO;
import com.example.demo.domain.dto.AvailabilityDTO;
import com.example.demo.domain.dto.CompetenceProfileDTO;
import com.example.demo.domain.entity.Application;
import com.example.demo.domain.entity.Availability;
import com.example.demo.domain.entity.Competence;
import com.example.demo.domain.entity.CompetenceProfile;
import com.example.demo.domain.entity.Person;
import com.example.demo.presentation.restException.AlreadyExistsException;
import com.example.demo.presentation.restException.CustomDatabaseException;
import com.example.demo.presentation.restException.FromDateAfterToDateException;
import com.example.demo.presentation.restException.PeriodAlreadyCoveredException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.AvailabilityInvalidException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.CompetenceProfileInvalidException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.PersonNotFoundException;
import com.example.demo.presentation.restException.EntryNotFoundExceptions.SpecificCompetenceNotFoundException;
import com.example.demo.repository.ApplicationRepository;
import com.example.demo.repository.AvailabilityRepository;
import com.example.demo.repository.CompetenceProfileRepository;
import com.example.demo.repository.CompetenceRepository;
import com.example.demo.repository.PersonRepository;


@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW) 
/**
 * Service for managing applications, competence profiles, and availability periods.
 * It handles CRUD operations and validation using various repositories.
 * It uses explicit transaction annotation to ensure a rollback occurs whenever an unchecked exception is thrown.
 */
public class ApplicationService {
    @Autowired
    private final ApplicationRepository applicationRepository;
    
    @Autowired
    private final CompetenceProfileRepository competenceProfileRepository;

    @Autowired
    private final AvailabilityRepository availabilityRepository;

    @Autowired
    private final CompetenceRepository competenceRepository;

    @Autowired
    private final PersonRepository personRepository;

    //We create the logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationService.class.getName()); 

    /**
     * Constructs a new instance of the ApplicationService (this is Spring boot managed).
     * 
     * @param applicationRepository the repository for accessing application database data
     * @param competenceProfileRepository the repository for accessing competence profile database data
     * @param availabilityRepository the repository for accessing availability database data
     * @param competenceRepository the repository for accessing competence database data 
     * @param personRepository
     */
    public ApplicationService(ApplicationRepository applicationRepository,CompetenceProfileRepository competenceProfileRepository,AvailabilityRepository availabilityRepository,CompetenceRepository competenceRepository,PersonRepository personRepository) {
        this.applicationRepository=applicationRepository;
        this.competenceProfileRepository=competenceProfileRepository;
        this.availabilityRepository=availabilityRepository;
        this.competenceRepository=competenceRepository;
        this.personRepository=personRepository;
    }

    /**
     * This function attempts to create a new competence profile for a specified competence, for a specific user and with the specified number of years of experience
     * 
     * @param competenceId The id for the competence this profile represents
     * @param personId The id for the person this profile is for
     * @param yearsOfExperience The number of years of experience the person states they have
     * @throws SpecificCompetenceNotFoundException this exception is thrown if the competence profile id provided does not match any in the database
     * @throws PersonNotFoundException this exception is thrown if no person exists with the specified the personId 
     * @throws CustomDatabaseException this is thrown if any of the jpa methods fail for some reason
     * @throws AlreadyExistsException if the competence profile already exists for this person
     * @return If no exception is thrown, the newly created competence profile is created and returned
     */
    public CompetenceProfileDTO CreateCompetenceProfile(Integer competenceId, Integer personId, Double yearsOfExperience)
    {
        CompetenceProfile newCompetenceProfile;
        Competence competence;
        Person person;
        try {
            Optional<Competence> competenceContainer = competenceRepository.findById(competenceId);
            if (competenceContainer.isEmpty()) {
                LOGGER.error("Failed to create new competence profile for person (`{}`) for competence (`{}`) with (`{}`) years of experience since no competence exists in the database with that id",personId,competenceId,yearsOfExperience);
                throw new SpecificCompetenceNotFoundException(competenceId);
            }
            competence=competenceContainer.get();

            Optional<Person> personContainer = personRepository.findById(personId);
            
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to create new competence profile for a person (`{}`) for competence (`{}`) with (`{}`) years of experience since no person exists in the database with that id",personId,competenceId,yearsOfExperience);
                throw new PersonNotFoundException(personId);
            }

            person=personContainer.get();

            if(competenceProfileRepository.existsByPersonAndCompetenceAndYearsOfExperience(person,competence,yearsOfExperience))
            {
                LOGGER.error("Failed to create competence profile for a person (`{}`) for competence (`{}`) with (`{}`) years of experience since an identical entry already exists",personId,competenceId,yearsOfExperience);
                throw new AlreadyExistsException("This competence profile already exists, so it does not need to be created");
            }

            newCompetenceProfile = new CompetenceProfile(person,competence,yearsOfExperience);

            competenceProfileRepository.save(newCompetenceProfile);
        }

        catch(DataAccessException e)
        {
            LOGGER.error("Failed to create competence profile for a person (`{}`) for competence (`{}`) with (`{}`) years of experience due to a database error : (`{}`)",personId,competenceId,yearsOfExperience,e.getMessage());
            throw new CustomDatabaseException();
        }
        
        LOGGER.info("Created new competence profile for person (`{}`) for competence (`{}`) with (`{}`) years of experience",personId,competence.getName(),yearsOfExperience);

        return newCompetenceProfile;
    }

    /**
     * This function returns a list of competence profiles for a specific person
     * 
     * @param personId The person id of the person to find competence profiles for
     * @throws PersonNotFoundException this exception is thrown if no person exists with the specified the person Id  
     * @throws CustomDatabaseException this exception is thrown is an error occurs when accessing the database
     * @return The list of competence profiles
     */
    public List<? extends CompetenceProfileDTO> GetCompetenceProfilesForAPerson(Integer personId)
    {
        List<? extends CompetenceProfileDTO> list;
        try {
            Optional<Person> personContainer = personRepository.findById(personId);
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to retrive competence profiles for a person with (`{}`) since no person with that id exists",personId);
                throw new PersonNotFoundException(personId);
            }
            Person person=personContainer.get();
            list = competenceProfileRepository.findAllByPerson(person);
            }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to retrive competence profiles for a person with (`{}`) due to a database error : (`{}`)",personId,e.getMessage());
            throw new CustomDatabaseException();
        }
        return list;
    }

    /**
     * This function attempts to create a new availability 
     * 
     * @param personId The person id of the person the availability period is for
     * @param fromDate This is the start date for this availability period
     * @param toDate This is the end of the availability period
     * @throws PersonNotFoundException this exception is thrown if no person exists with the specified the person Id  
     * @throws FromDateAfterToDateException this exception is thrown if the from date is after the to date
     * @throws PeriodAlreadyCoveredException this exception is thrown if the new periods date range is fully covered by an existing availability period
     * @throws AlreadyExistsException if an identical availability period already exists
     * @throws CustomDatabaseException this exception is thrown is an error occurs when accessing the database
     * @return If no exception is thrown, this returns the newly created availability
     */
    public AvailabilityDTO CreateAvailability(Integer personId, Date fromDate, Date toDate)
    {
        Availability newAvailability;
        try {

            Optional<Person> personContainer = personRepository.findById(personId);
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to create availability period for a person with (`{}`) from (`{}`) to (`{}`) since no person with that id exists",personId,fromDate,toDate);
                throw new PersonNotFoundException(personId);
            }
            Person person=personContainer.get();

            if(fromDate.after(toDate))
            {
                LOGGER.error("Failed to create availability period for a person with (`{}`) from (`{}`) to (`{}`) since from date is after to date",personId,fromDate,toDate);
                throw new FromDateAfterToDateException(fromDate,toDate);
            }

            if(availabilityRepository.existsByFromDateAndToDateAndPerson(fromDate, toDate, person))
            {
                LOGGER.error("Failed to create availability period for a person with (`{}`) from (`{}`) to (`{}`) since an identical entry already exists",personId,fromDate,toDate);
                throw new AlreadyExistsException("This availability period already exists, so it does not need to be created");
            }

            if (availabilityRepository.existsByFromDateLessThanEqualAndToDateGreaterThanEqualAndPerson(fromDate, toDate,person)) {
                LOGGER.error("Failed to create availability period for a person with (`{}`) from (`{}`) to (`{}`) since date range fully covered by existing availability period",personId,fromDate,toDate);
                throw new PeriodAlreadyCoveredException(fromDate, toDate);
            }

            newAvailability=new Availability(person, fromDate, toDate);
            availabilityRepository.save(newAvailability);
                
        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to create availability period for a person with (`{}`) from (`{}`) to (`{}`) due to a database error : (`{}`)",personId,fromDate,toDate,e.getMessage());
            throw new CustomDatabaseException();
        }

        LOGGER.info("Created new availability period for person (`{}`) from (`{}`) to (`{}`)",personId,fromDate,toDate);
        return newAvailability;
    }

    /**
     * This function returns a list of competence profiles for a specific person
     * 
     * @param personId The person id of the person to find competence profiles for
     * @throws PersonNotFoundException this exception is thrown if no person exists with the specified the person Id  
     * @throws CustomDatabaseException this exception is thrown if an error occurs with the database
     * @return The list of competence profiles
     */
    public List<? extends AvailabilityDTO> GetAvailabilityForAPerson(Integer personId)
    {
        List<? extends AvailabilityDTO> list;
        try {
            Optional<Person> personContainer = personRepository.findById(personId);
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to retrive availability periods for a person with (`{}`) since no person with that id exists",personId);
                throw new PersonNotFoundException(personId);
            }
            Person person=personContainer.get();
            list= availabilityRepository.findAllByPerson(person);

        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to retrive competence profiles for a person with (`{}`) due to a database error : (`{}`)",personId,e.getMessage());
            throw new CustomDatabaseException();
        }

        return list;

    }

    /**
     * This function creates a new application for a specified person with specific availability periods and competence profiles
     * 
     * @param personId
     * @param availabilityIds
     * @param competenceProfileIds
     * @throws PersonNotFoundException This is thrown if a person with the specified id could not be found
     * @throws AvailabilityInvalidException This is thrown if one of the availabilites could not be found or where invalid for the specified user
     * @throws CompetenceProfileInvalidException This is thrown if one of the competence profiles could not be found or where invalid for the specified user 
     * @throws CustomDatabaseException this exception is thrown if an error occurs with the database
     * @return If it does not throw any exceptions, it will return the newly created competence profile
     */
    public ApplicationDTO SubmitApplication(Integer personId,List<Integer> availabilityIds,List<Integer> competenceProfileIds)
    {
        Application newApplication;

        try {
            Optional<Person> personContainer = personRepository.findById(personId);
            if (personContainer.isEmpty()) {
                LOGGER.error("Failed to create application for a person (`{}`) since no person with that id exists",personId);
                throw new PersonNotFoundException(personId);
            }
            Person person=personContainer.get();

            List<Availability> availabilities=new ArrayList<Availability>();

            if (availabilityIds.size()==0) {
                LOGGER.error("Failed to create application for a person (`{}`) since the availabiltyIds is empty",personId);
                throw new AvailabilityInvalidException("No availability period was specified, please specify at least one for this application");
            }

            for (Integer i : availabilityIds) {
                Optional<Availability> availabilityContainer = availabilityRepository.findById(i);
                if (availabilityContainer.isEmpty()) {
                    LOGGER.error("Failed to create application for a person (`{}`) since (at least) one of the provied availabilites do not exist, specifically requested with id (`{}`) which does not exist",personId,i);
                    throw new AvailabilityInvalidException("No availability with id "+i+" in the database");
                }
                if (availabilityContainer.get().getPerson().getId()!=person.getId()) {
                    LOGGER.error("Failed to create application for a person (`{}`) since (at least) one of the provied availabilites profiles belongs to another users, specifically requested with id (`{}`)",personId,i);
                    throw new AvailabilityInvalidException("The availability period with id "+i+" belongs to another user");
                }
                availabilities.add(availabilityContainer.get());
            }

            if (applicationRepository.existsByAvailabilityPeriodsForApplicationAndApplicant(availabilities, person)) {
                LOGGER.error("Failed to create application for a person (`{}`) since they already have an application with the exact same availability periods");
                throw new AvailabilityInvalidException("You already have an application with the exact same availability period(s)");
            }

            List<CompetenceProfile> competenceProfiles=new ArrayList<CompetenceProfile>();

            if (competenceProfileIds.size()==0) {
                LOGGER.error("Failed to create application for a person (`{}`) since the competenceProfileIds is empty");
                throw new CompetenceProfileInvalidException("No competence profile was specified, please specify at least one for this application");
            }

            for (Integer i : competenceProfileIds) {
                Optional<CompetenceProfile> competenceProfilesContainer = competenceProfileRepository.findById(i);
                if (competenceProfilesContainer.isEmpty()) {
                    LOGGER.error("Failed to create application for a person (`{}`) since (at least) one of the provied competence profiles do not exist, specifically requested with id (`{}`)",personId,i);
                    throw new CompetenceProfileInvalidException("No competence profile with id "+i+" in the database");
                }
                if (competenceProfilesContainer.get().getPerson().getId()!=person.getId()) {
                    LOGGER.error("Failed to create application for a person (`{}`) since (atleast) one of the provied competence profiles belongs to another users, specifically requested with id (`{}`)",personId,i);
                    throw new CompetenceProfileInvalidException("The competence profile with id "+i+" belongs to another user");
                }
                competenceProfiles.add(competenceProfilesContainer.get());
            }


            newApplication=new Application(person,availabilities,competenceProfiles);
            applicationRepository.save(newApplication);

        }
        catch(DataAccessException e)
        {
            LOGGER.error("Failed to create application for a person (`{}`) due to a database error : (`{}`)",personId,e.getMessage());
            throw new CustomDatabaseException();
        }

        LOGGER.info("Created new application period for person (`{}`), it has id ",personId, newApplication.getApplicationId());

        return newApplication;
    }

}
