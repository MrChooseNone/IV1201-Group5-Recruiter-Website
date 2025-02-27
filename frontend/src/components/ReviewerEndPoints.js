import React, { useState, useEffect } from "react";
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

const API_URL = "http://localhost:8080/review"; // Adjust for your backend

export default function ReviewerDashboard() {
    const [applications, setApplications] = useState([]);
    const [status, setStatus] = useState("unchecked")
    const [applicationsByStatus, setApplicationsByStatus] = useState([]);

    const fetchApplicants = () => {
        const url = `${API_URL}/getApplications`
        fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json"
            }
        })
        .then((response) => response.json())
        .then((data) => {
            setApplications(data);
        })
        .catch((error) => {
            console.error("Failed to fetch applications: " + error);
        })
    }

    const fetchApplicantsByStatus = () => {
        const url = `${API_URL}/getApplicationsByStatus/${status}`
        fetch(url, {
            method: "GET",
            headers: {
                "Content-type": "application/json"
            }
        })
        .then((response) => response.json())
        .then((data) => {
            setApplicationsByStatus(data);
        })
        .catch((error) => {
            console.error("Failed to fetch applicants, please use values unchecked, accepted, denied: " + error);
        })
    }

    return(
        <Container sx={{
            bgcolor: "#AFF9C9",
            display: "flex",
            p: 4,
            borderRadius: 4,
            m:2,
        }}>
            {/*Get all applications */}
            <Paper elevation={3} sx={{ padding: "20px", marginBottom: "20px", bgcolor: "#67E0A3" }}>
                <Button variant="contained" onClick={fetchApplicants}>Get all applications</Button>
                <List>
                    {applications.map((allApplications) => {
                        return (
                            <ListItem key={allApplications.applicationId}>
                                <ListItemText
                                primary={`Applicant: ${allApplications.applicant.name + " " + allApplications.applicant.surname}, Status: ${allApplications.applicationStatus}`}
                                secondary={`Submition date: ${allApplications.applicationDate}` }
                                />
                            </ListItem>
                        );
                    })}
                </List>
            </Paper>

            {/*Get all applications by status */}
            <Paper elevation={3} sx={{ padding: "20px", marginBottom: "20px", bgcolor: "#67E0A3" }}>
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
                                    <ListItemText
                                    primary={`Applicant: ${statusApplicants.applicant.name + " " + statusApplicants.applicant.surname}, Status: ${statusApplicants.applicationStatus}`}
                                    secondary={`Submition date: ${statusApplicants.applicationDate}` }
                                    />
                                    <Button variant="contained" component={Link} to={`/applicant/${statusApplicants.applicationId}`}>View Applicant</Button>
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