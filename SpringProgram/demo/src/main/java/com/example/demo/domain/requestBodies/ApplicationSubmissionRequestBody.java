package com.example.demo.domain.requestBodies;

import java.util.List;

/**
 * This class defines the request body for the endpoint submitApplication
 * Below is an example of a valid json body which could be parsed into this class:
 * {
    "personId":1014,
    "availabilityIds":[20872],
    "competenceProfileIds":[1234]
    }
 */
public class ApplicationSubmissionRequestBody {
    private Integer personId;
    private List<Integer> availabilityIds;
    private List<Integer> competenceProfileIds;

    /**
     * This is the default constructor
     */
    public ApplicationSubmissionRequestBody()
    {}

    /**
     * This is a complete constructor
     * @param personId the person's ID
     * @param availabilityIds list of availability IDs
     * @param competenceProfileIds list of competence profile IDs
     */
    public ApplicationSubmissionRequestBody(Integer personId,List<Integer> availabilityIds, List<Integer> competenceProfileIds)
    {
        this.personId=personId;
        this.availabilityIds=availabilityIds;
        this.competenceProfileIds=competenceProfileIds;
    }

    /**
     * This is a getter for person id
     * @return this instances person id
     */
    public Integer getPersonId()
    {
        return this.personId;
    }
    
    /**
     * This is a getter for the availability id list
     * @return this instances list of availability ids
     */
    public List<Integer> getAvailabilityIds()
    {
        return this.availabilityIds;
    }

    /**
     * This is a getter for the competence profiles id list
     * @return this instances list of competence profiles ids
     */
    public List<Integer> getCompetenceProfileIds()
    {
        return this.competenceProfileIds;
    }
}
