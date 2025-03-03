import React, {useState, useEffect} from 'react';
import { useParams } from "react-router-dom";
import Box from '@mui/material/Box';

import Button from '@mui/material/Button';

import { Typography, Divider, CircularProgress, List, ListItem } from '@mui/material';
import { ThemeContext } from '@emotion/react';

export default function ApplicationDetailsComp() {
    const { id } = useParams();
    const[application,setApplication] = useState(null);
    const [status, setStatus] = useState("unchecked");
    const [isPressedAccepted, setIsPressedAccepted] = useState(false);
    const [isPressedDenied, setIsPressedDenied] = useState(false);

    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;
 
    // Hämta kompetenser automatiskt vid sidladdning
    useEffect(() => {
        fetch(`${API_URL}/review/getApplicationsById/${id}`, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
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
        })
    }, [id]);

    if (!application) {
        return <CircularProgress></CircularProgress>;
    }

    const UpdateStatus = () => {
        
        
        fetch(`${API_URL}/review/updateApplicationStatus`, {
            method: "POST",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
                "Content-Type": "application/x-www-form-urlencoded", 
            },
            body: new URLSearchParams({
                applicationId: id,
                status: status,
                versionNumber: application.versionNumber
            })
            
        })
        .then((response) => { 
            if (response.ok) {
                return response.text(); // Parse JSON if response is OK
            } else {
                return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
            }
        }) // Parse response as text
        .then((data) => {
            console.log(data); // Write data
        })
        .catch((error) => {
            console.error("Error adding applicant:", error);
        });
    }

    const handleAccept = () => {
        setStatus("accepted");
        setIsPressedAccepted(!isPressedAccepted);
        setIsPressedDenied(false);
    }

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
        borderRadius: 2,
        alignItems: "center",
        }}
    >
        <Typography variant='h4'>{application.applicant.name + " " + application.applicant.surname}</Typography>
        <Typography variant='h6'>{"E-mail: " + application.applicant.email}</Typography>
        <Typography variant='h6'>{"Person number: " + application.applicant.pnr}</Typography>
        <Divider></Divider>
        <Typography variant='h6'>{"Status: " +application.applicationStatus}</Typography>
        <Typography variant='h6'>{"Submition date: " +application.applicationData}</Typography>
        <Typography variant='h6'>{"Version: " +application.versionNumber}</Typography>
        <Divider></Divider>
         
        <Box sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            p: 4,
            borderRadius: 2,
            alignItems: "center",
        }}>
            <Typography variant='h6'>Competence Profiles</Typography>
            <List>
                {application.competenceProfilesForApplication.length > 0 ? (
                    application.competenceProfilesForApplication.map((competences) => {
                        return(
                            <ListItem key={competences.competenceProfileId}>
                                <Box>
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

        <Box sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            p: 4,
            borderRadius: 2,
            alignItems: "center",
        }}>
            <Typography variant='h6'>Availability</Typography>
            <List>
                {application.availabilityPeriodsForApplication.length > 0 ? (
                    application.availabilityPeriodsForApplication.map((availability) => {
                        return (
                            <ListItem key={availability.availabilityId}>
                                <Box>
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