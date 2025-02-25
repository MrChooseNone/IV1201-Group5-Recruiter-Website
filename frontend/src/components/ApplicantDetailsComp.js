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

    
    const url = `http://localhost:8080/review/getApplicationsById/${id}`;

    console.log("Fetching Application from:", url); 
 
    // Hämta kompetenser automatiskt vid sidladdning
    useEffect(() => {
        fetch(url, {
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
        <Typography variant='h6'>{"Status: " +application.applicationStatus}</Typography>
    </Box>
  );
}