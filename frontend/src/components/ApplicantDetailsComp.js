import React, {useState, useEffect} from 'react';
import { useParams } from "react-router-dom";
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import List from '@mui/material/List';
import { Typography, Divider, CircularProgress } from '@mui/material';

export default function ApplicationDetailsComp() {
    const { id } = useParams();
    const[application,setApplication] = useState(null);
    const [versionNumber, setVersionNumber] = useState("0");
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
        .then((response) => response.json())
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
        setVersionNumber(application.versionNumber)
        
        fetch(`${API_URL}/review/updateApplicationStatus`, {
            method: "POST",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
                "Content-Type": "application/x-www-form-urlencoded", 
            },
            body: new URLSearchParams({
                applicationId: id,
                status: status,
                versionNumber: versionNumber.toString()
            })
            
        })
        .then((response) => response.text()) // Parse response as text
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
      bgcolor: "#8E8C8C",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center",}}
      
      
    >
        <Typography variant='h4'>{application.applicant.name + " " + application.applicant.surname}</Typography>
        <Typography variant='h6'>{"E-mail: " + application.applicant.email}</Typography>
        <Typography variant='h6'>{"Person number: " + application.applicant.pnr}</Typography>
        <Divider></Divider>
        <Typography variant='h6'>{"Status: " +application.applicationStatus}</Typography>
        <Typography variant='h6'>{"Submition date: " +application.applicationData}</Typography>
        <Typography variant='h6'>{"Version: " +application.versionNumber}</Typography>
        <Divider></Divider>
        
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
        <Button variant='contained' onClick={UpdateStatus} sx={{
            m: 1
        }}> submit</Button>
    </Box>
  );
}