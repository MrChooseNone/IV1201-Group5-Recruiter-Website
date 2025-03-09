import React, { useState, useContext } from "react";
import {
  Container,
  TextField,
  Button,
  Typography,
  Paper,
  Box,
  List,
  ListItem,
  ListItemText,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  CircularProgress
} from "@mui/material";
import { Link } from "react-router-dom";
import { isTokenExpired } from "./utils/TokenChecker";
import { useNavigate } from "react-router-dom";

import { AuthContext } from '../App';

export default function ReviewerDashboard() {
    const [applications, setApplications] = useState([]);
    const [status, setStatus] = useState("unchecked");
    const [applicationsByStatus, setApplicationsByStatus] = useState([]);

    const navigate = useNavigate();

    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;

    //Here we import the auth information from the context
    const { auth, setAuth } = useContext(AuthContext);
    
    const fetchApplicants = () => {
        if(isTokenExpired(sessionStorage.getItem("token"))){ //if token has expired 
            setAuth({});
            sessionStorage.clear();
            alert("Your session has expired. Please log in again.");
            navigate("/login"); // Redirect to login page
        }
        const url = `${API_URL}/review/getApplications`
        console.log(auth.token);
        fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json",
                "Authorization": "Bearer "+auth.token
            }
        })
        .then((response) => { 
            if (response.ok) {
                return response.json(); // Parse JSON if response is OK
            } else {
                if(response.status==401) //This checks if the user was not authorized
                    {
                        alert("You are not authorized to requests applications");
                        throw new Error(`Unauthorized fetch`); 
                    }
                    else
                    {
                        return response.text().then((errorText) => { 
                        throw new Error(`Failed to fetch: ${errorText}`); 
                    });
                    }
            }
        })
        .then((data) => {
            setApplications(data);
        })
        .catch((error) => {
            console.error("Failed to fetch applications: " + error);
        })
    }

    const fetchApplicantsByStatus = () => {
        if(isTokenExpired(sessionStorage.getItem("token"))){ //if token has expired 
            setAuth({});
            sessionStorage.clear();
            alert("Your session has expired. Please log in again.");
            navigate("/login"); // Redirect to login page
        }
        const url = `${API_URL}/review/getApplicationsByStatus/${status}`
        fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json",
                "Authorization": "Bearer "+auth.token
            }
        })
        .then((response) => { 
            if (response.ok) {
                return response.json(); // Parse JSON if response is OK
            } else {
                if(response.status==401) //This checks if the user was not authorized
                {
                    alert("You are not authorized to requests applications by status");
                    throw new Error(`Unauthorized fetch`); 
                }
                else
                {
                    return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
                }
                
            }
        })
        .then((data) => {
            setApplicationsByStatus(data);
        })
        .catch((error) => {
            console.error("Failed to fetch applications by status, please use values unchecked, accepted, denied: " + error);
        })
    }

    return(
        <Container sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            flexDirection: {xs: "column", md: "row"},
            gap: 2,
            justifySelf: "center",
            
            overflow: "hidden"
        }}>
            {/*Get all applications */}
            <Paper elevation={3} sx={{ 
                p: 1, 
                marginBottom: "20px", 
                bgcolor: "#67E0A3",
                display: "flex",
                flexDirection: "column",
                marginTop: 2,
                borderRadius: 4,
                top: 0, 
                alignContent: "center",
                alignItems: "center",
                textAlign: "center"

            }}>
                <Button variant="contained" onClick={fetchApplicants} sx={{top: 2}}>Get all applications</Button>
                <List>
                    {applications.length > 0 ? (
                        applications.map((Applicants) => {
                            return (
                                <ListItem key={Applicants.applicationId}>
                                        <Box
                                              component="form"
                                              sx={{
                                                bgcolor: "#AFF9C9",
                                                display: "flex",
                                                flexDirection: "column",
                                                justifyContent: "center",
                                                alignItems: "center",
                                                width: "100%",
                                                height: "auto",
                                                p: 1,
                                                  borderRadius: 2,
                                                }}
                                              >
                                                <Typography variant="h6" sx={{
                                                    justifyContent: "center",
                                                    alignItems: "center",
                                                    
                                                    }}>
                                                    Applicant: {Applicants.applicant.name + " " + Applicants.applicant.surname}
                                                </Typography>
                                                <Typography variant="h8" sx={{
                                                    color: "secondary.main",
                                                    justifyContent: "center",
                                                    alignItems: "center",}}>
                                                    Status: {Applicants.applicationStatus}
                                                </Typography>
                                                <Typography variant="h9" sx={{
                                                    justifyContent: "center",
                                                    alignItems: "center",
                                                    
                                                    }}>
                                                    Submition date: {Applicants.applicationDate}
                                                </Typography>
                                        <Button variant="contained" component={Link} to={`/applicant/${Applicants.applicationId}`} sx={{m: 1}}>View Applicant</Button>
                                    </Box>
                                </ListItem>
                            );
                        })

                    ) : (
                        <Typography variant="h6">No applicants with that status</Typography>
                    )}
                </List>
            </Paper>

            {/*Get all applications by status */}
            <Paper elevation={3} sx={{
                p: 1, 
                marginBottom: "20px", 
                bgcolor: "#67E0A3",
                display: "flex",
                flexDirection: "column",
                marginTop: 2,
                borderRadius: 4,
                top: 0,                
                alignContent: "center",
                alignItems: "center",
                textAlign: "center"

                 }}>
                <FormControl fullWidth margin="dense">
                    <Select value={status} onChange={(e) => setStatus(e.target.value)}>
                        <MenuItem key={"unchecked"} value={"unchecked"}>Unchecked</MenuItem>  
                        <MenuItem key={"accepted"} value={"accepted"}>Accepted</MenuItem> 
                        <MenuItem key={"denied"} value={"denied"}>Denied</MenuItem>                                          
                    </Select>
                </FormControl>
                <Button variant="contained" onClick={fetchApplicantsByStatus}>Get applications by Status: {status}</Button>
                <List>
                    {applicationsByStatus.length > 0 ? (
                        applicationsByStatus.map((statusApplicants) => {
                            return (
                                <ListItem key={statusApplicants.applicationId}>
                                        <Box
                                              component="form"
                                              sx={{
                                                bgcolor: "#AFF9C9",
                                                display: "flex",
                                                flexDirection: "column",
                                                justifyContent: "center",
                                                alignItems: "center",
                                                width: "100%",
                                                height: "auto",
                                                p: 1,
                                                  borderRadius: 2,
                                                }}
                                              >
                                                <Typography variant="h6" sx={{
                                                    justifyContent: "center",
                                                    alignItems: "center",
                                                    
                                                    }}>
                                                    Applicant: {statusApplicants.applicant.name + " " + statusApplicants.applicant.surname}
                                                </Typography>
                                                <Typography variant="h8" sx={{
                                                    color: "secondary.main",
                                                    justifyContent: "center",
                                                    alignItems: "center",}}>
                                                    Status: {statusApplicants.applicationStatus}
                                                </Typography>
                                                <Typography variant="h9" sx={{
                                                    justifyContent: "center",
                                                    alignItems: "center",
                                                    
                                                    }}>
                                                    Submition date: {statusApplicants.applicationDate}
                                                </Typography>
                                        <Button variant="contained" component={Link} to={`/applicant/${statusApplicants.applicationId}`} sx={{m: 1}}>View Applicant</Button>
                                    </Box>
                                </ListItem>
                            );
                        })

                    ) : (
                        <Typography variant="h6">No applicants with that status</Typography>
                    )}
                </List>
            </Paper>


        </Container>
    );

}