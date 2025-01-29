package com.example.demo.domain.dto;

import java.sql.Date;

/* 
 * The AvailabilityDTO interface provides a data transfer interface for availability periods
*/
public interface AvailabilityDTO {
    /** Interface function for retriving the availability id
     * 
     * @return the availability id
     */
    public Integer getAvailabilityId();

    /** Interface function for retriving the person for this availability period
     * 
     * @return the person this period is for
     */
    public PersonDTO getPerson();

    /** Interface function for retriving the date this availability starts at
     * 
     * @return the date this period starts at
     */
    public Date getFromDate();
    
    /** Interface function for retriving the date this availability ends at
     * 
     * @return the date this period ends at
     */
    public Date getToDate();
}
