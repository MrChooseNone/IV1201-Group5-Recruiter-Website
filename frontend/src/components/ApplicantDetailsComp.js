import React, {useState, useEffect,useContext} from 'react';
import { useParams } from "react-router-dom";
import Box from '@mui/material/Box';

import Button from '@mui/material/Button';

import { Typography, Divider, CircularProgress, List, ListItem } from '@mui/material';

import { AuthContext } from '../App';

/**
 * This component is responsible for handling the detailed view of an application
 * @returns The component itself, as a function
 */
export default function ApplicationDetailsComp() {
    const { id } = useParams();
    const[application,setApplication] = useState(null);
    const [status, setStatus] = useState("unchecked");
    const [isPressedAccepted, setIsPressedAccepted] = useState(false);
    const [isPressedDenied, setIsPressedDenied] = useState(false);

    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;

  const {auth,setAuth} = useContext(AuthContext);

 
    // Hämta kompetenser automatiskt vid sidladdning
    useEffect(() => {
        fetch(`${API_URL}/review/getApplicationsById/${id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${auth.token}`, 
            }
        })
        .then((response) => { 
            if (response.ok) {
                return response.json(); // Parse JSON if response is OK
            } else {
                return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
            }
        })
        .then((data) => {
            console.log(data);
            setApplication(data);
            
        })
        .catch((error) => {
            console.error("faild to get application by id: " + error);
            alert(error);
        })
    }, [id]);

    if (!application) {
        return <CircularProgress></CircularProgress>;
    }

    /**
     * This is responsible for sending a request to update the application status, 
     * with it first performing some logical checks (is the new status the same as the current status), 
     * sending a request to update if not, updating the shown application if the updated worked or showing an error alert to the user if not
     * @returns Nothing
     */
    const UpdateStatus = () => {
        
        //We check here if the new status is the same as the current status
        if(application.applicationStatus==status)
        {
            alert("Application status is already "+status)
            return;
        }

        fetch(`${API_URL}/review/updateApplicationStatus`, {
            method: "POST",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
                "Content-Type": "application/x-www-form-urlencoded", 
                "Authorization": `Bearer ${auth.token}`
            },
            body: new URLSearchParams({
                applicationId: id,
                status: status,
                versionNumber: application.versionNumber
            })
            
        })
        .then((response) => { 
            if (response.ok) {
                //If it was ok we send an alert and update the application based on the response
                alert("Application updated to " + status)
                return response.json().then((text) => { 
                    setApplication(text);
                });
            } 
            else {
                if(response.status==404)
                    {
                        return response.text().then((errorText) => { 
                            throw new Error(`${errorText}`); 
                        });
                    }
                else
                {
                    return response.text().then((errorText) => { 
                        throw new Error(`Failed to fetch: ${errorText}`); 
                    });
                }
            }
        }) // Parse response as text
        .then((data) => {
            console.log(data); // Write data
        })
        .catch((error) => {
            console.error("Error updating applicant status:", error);
            alert(error);
        });
    }

    //This updates the status of the stateful values when the accept button is pressed
    const handleAccept = () => {
        setStatus("accepted");
        setIsPressedAccepted(!isPressedAccepted);
        setIsPressedDenied(false);
    }

    //This updates the status of the stateful values when the decline button is pressed
    const handleDecline = () => {
        setStatus("denied");
        setIsPressedDenied(!isPressedDenied);
        setIsPressedAccepted(false);
    }

  return (
    <Box
      component="form"
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' }, 
        bgcolor: "#AFF9C9",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        p: 4,
        gap: 2,
        borderRadius: 2,
        alignItems: "center",
        
        }}
    >
        <Typography variant='h4'>{application.applicant.name + " " + application.applicant.surname}</Typography>
        <Typography variant='h6'>{"E-mail: " + application.applicant.email}</Typography>
        <Typography variant='h6'>{"Person number: " + application.applicant.pnr}</Typography>
        <Typography variant='h6'>{"Status: " +application.applicationStatus}</Typography>
        <Typography variant='h6'>{"Submission date: " +application.applicationDate}</Typography>
        <Typography variant='h6'>{"Version: " +application.versionNumber}</Typography>
        <Divider flexItem  sx={{bgcolor: "#67E0A3", height: 2, width: "75%", alignSelf: "center"}}></Divider>
         
        <Box  sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            
            p: 2,
            borderRadius: 2,
            
        }}>
            <Typography variant='h6'>Competence Profiles</Typography>
            <List sx={{display: "flex", flexDirection: "column", alignItems: "center"}}>
                {application.competenceProfilesForApplication.length > 0 ? (
                    application.competenceProfilesForApplication.map((competences) => {
                        return(
                            <ListItem sx={{justifyContent: "center"}} key={competences.competenceProfileId}>
                                <Box sx={{boxShadow: 5, p: 2, borderRadius: 2, justifySelf: "center",}}>
                                    <Typography>{"Competence: "+competences.competenceDTO.name}</Typography>
                                    <Typography>{"Years of experience: "+competences.yearsOfExperience} </Typography>
                                </Box>
                            </ListItem>
                        );
                    })

                ) : (
                    <Typography>No competences</Typography>
                )}
            </List>
        </Box>
        <Divider flexItem  sx={{bgcolor: "#67E0A3", height: 2, width: "75%", alignSelf: "center"}}></Divider>
        <Box sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            alignContent: "center",
            justifyItems: "center",
            p: 4,
            borderRadius: 2,
            
        }}>
            <Typography variant='h6'>Availability</Typography>
            <List sx={{display: "flex", flexDirection: "column", alignItems: "center"}}>
                {application.availabilityPeriodsForApplication.length > 0 ? (
                    application.availabilityPeriodsForApplication.map((availability) => {
                        return (
                            <ListItem sx={{justifyContent: "center"}} key={availability.availabilityId}>
                                <Box sx={{boxShadow: 5, p: 2, borderRadius: 2}}>
                                    <Typography>{"From date: "+availability.fromDate} </Typography>
                                    <Typography>{"To date :"+availability.toDate} </Typography>
                                </Box>
                            </ListItem>
                        );
                    })
                ) : (
                    <Typography>No availability</Typography>
                )}
            </List>
        </Box>
        <Divider flexItem  sx={{bgcolor: "#67E0A3", height: 2, width: "75%", alignSelf: "center"}}></Divider>
        <Button onClick={handleAccept} variant='contained' sx={{
            bgcolor: isPressedAccepted ? "success.main" : "primary",
            transform: isPressedAccepted ? "translateY(2px)" : "none",
            m: 1
        }}>Accept!</Button>
        <Button onClick={handleDecline} variant='contained' sx={{
            bgcolor: isPressedDenied ? "#D33F49" : "primary",
            transform: isPressedDenied ? "translateY(2px)" : "none",
            m: 1,
        }}>Decline</Button>
        <Button disabled={!isPressedAccepted && !isPressedDenied} variant='contained' onClick={UpdateStatus} sx={{
            m: 1
        }}> submit</Button>
    </Box>
  );
}