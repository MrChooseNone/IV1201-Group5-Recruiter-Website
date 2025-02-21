import React, { useState } from "react";
import {
  Container,
  TextField,
  Button,
  Typography,
  Paper,
  
  List,
  ListItem,
  ListItemText,
} from "@mui/material";

const API_URL = "http://localhost:8080/application"; // Adjust for your backend

export default function ApplicationEndPoint() {
    const [personId, setPersonId] = useState("");
    const [competenceProfiles, setCompetenceProfiles] = useState([]);
    const [competenceId, setCompetenceId] = useState("");
    const [yearsOfExperience, setYearsOfExperience] = useState("");
    const [availability, setAvailability] = useState([]);
    const [fromDate, setFromDate] = useState("");
    const [toDate, setToDate] = useState("");
    const [applicationResult, setApplicationResult] = useState(null);

    // Fetch competence profiles
    const getCompetenceProfiles = async () => {
        const response = await fetch(`${API_URL}/getAllCompetenceProfiles?personId=${personId}`);
        if (response.ok) {
        setCompetenceProfiles(await response.json());
        } else {
        alert("Failed to fetch competence profiles");
        }
    };

    // Create a new competence profile
    const createCompetenceProfile = async () => {
        const response = await fetch(`${API_URL}/createCompetenceProfile?personId=${personId}&competenceId=${competenceId}&yearsOfExperience=${yearsOfExperience}`, {
        method: "POST",
        });
        if (response.ok) {
        alert("Competence profile created successfully!");
        } else {
        alert("Failed to create competence profile");
        }
    };

    // Fetch availability periods
    const getAvailability = async () => {
        const response = await fetch(`${API_URL}/getAllAvailability?personId=${personId}`);
        if (response.ok) {
        setAvailability(await response.json());
        } else {
        alert("Failed to fetch availability periods");
        }
    };

    // Create a new availability period
    const createAvailability = async () => {
        const response = await fetch(`${API_URL}/createAvailability?personId=${personId}&fromDate=${fromDate}&toDate=${toDate}`, {
        method: "POST",
        });
        if (response.ok) {
        alert("Availability period created successfully!");
        } else {
        alert("Failed to create availability");
        }
    };


    return (
        

            <Container maxWidth="md" sx={{ 
                marginTop: "20px",
                bgcolor: "#AFF9C9",
            }}>
            
            
            <Paper elevation={3} style={{ padding: "20px", marginBottom: "20px" }}>
                <Typography variant="h6">Competence Profiles</Typography>
                <TextField label="Person ID" value={personId} onChange={(e) => setPersonId(e.target.value)} fullWidth />
                <Button variant="contained" color="primary" onClick={getCompetenceProfiles} style={{ marginTop: "10px" }}>
                Get Competence Profiles
                </Button>
                <List>
                {competenceProfiles.map((cp, index) => (
                    <ListItem key={index}>
                    <ListItemText primary={`Competence ID: ${cp.competenceId}, Experience: ${cp.yearsOfExperience} years`} />
                    </ListItem>
                ))}
                </List>
            </Paper>

            <Paper elevation={3} style={{ padding: "20px", marginBottom: "20px" }}>
                <Typography variant="h6">Create Competence Profile</Typography>
                <TextField label="Competence ID" value={competenceId} onChange={(e) => setCompetenceId(e.target.value)} fullWidth />
                <TextField label="Years of Experience" value={yearsOfExperience} onChange={(e) => setYearsOfExperience(e.target.value)} fullWidth />
                <Button variant="contained" color="secondary" onClick={createCompetenceProfile} style={{ marginTop: "10px" }}>
                Create Profile
                </Button>
            </Paper>

            <Paper elevation={3} style={{ padding: "20px", marginBottom: "20px" }}>
                <Typography variant="h6">Availability</Typography>
                <Button variant="contained" color="primary" onClick={getAvailability}>
                Get Availability Periods
                </Button>
                <List>
                {availability.map((a, index) => (
                    <ListItem key={index}>
                    <ListItemText primary={`From: ${a.fromDate}, To: ${a.toDate}`} />
                    </ListItem>
                ))}
                </List>
            </Paper>

            <Paper elevation={3} style={{ padding: "20px", marginBottom: "20px" }}>
                <Typography variant="h6">Create Availability</Typography>
                <Typography variant="h8">From date</Typography>
                <TextField type="date"  value={fromDate} onChange={(e) => setFromDate(e.target.value)} fullWidth />
                <Typography variant="h8">To date</Typography>
                <TextField type="date"  value={toDate} onChange={(e) => setToDate(e.target.value)} fullWidth />
                <Button variant="contained" color="secondary" onClick={createAvailability} style={{ marginTop: "10px" }}>
                Create Availability
                </Button>
            </Paper>

            <Paper elevation={3} style={{ padding: "20px" }}>
                
                <Button variant="contained" color="primary" >
                Submit Application
                </Button>
                
            </Paper>
            </Container>
        
    );
};

