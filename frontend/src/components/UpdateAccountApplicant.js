import React, { useState} from "react";
import { TextField, Button, Container, Typography, Box } from "@mui/material";


const ApplicantForm = () => {
    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;

    const [email, setEmail] = useState("");
    const [resetToken, setResetToken] = useState("");
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");

    

    const RequestReset = (e) => {
        e.preventDefault(); // Prevents page refresh

        const params = new URLSearchParams();
        params.append("email", email);
        
        const url = `${API_URL}/person/requestApplicantReset?${params.toString()}`;
        
        console.log("Sending: ", url); // remove (just debug)
        
        fetch(url, {
            method: "POST",
            headers: {
                
                "Content-Type": "application/json", 
                
            },
            
        })
        .then((response) => { 
            if (response.ok) {
                return response.text(); // Parse JSON if response is OK
                
            } else {
                return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
            }
            }) 
        .then((data) => {
            console.log(data);
            alert("Check email for token");

        })
        .catch((error) => {
            console.error("Error finding email:", error);
            alert("Email does not exist")
        });
        
    };

    const ResetPassword = (e) => {
        e.preventDefault(); // Prevents page refresh

        const params = new URLSearchParams();
        params.append("resetToken", resetToken);
        params.append("username", username);
        params.append("password", password);
        
        const url = `${API_URL}/person/updateApplicant?${params.toString()}`;
        
        console.log("Sending: ", url); // remove (just debug)
        
        fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json", 
                
            },
            
        })
        .then((response) => { 
            if (response.ok) {
                return response.text(); // Parse JSON if response is OK
                
            } else {
                return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
            }
            }) 
        .then((data) => {
            console.log(data);
            
            setMessage(data);
        })
        .catch((error) => {
            console.error("Error:", error);
            alert("Reset token is incorrect or username is taken")
        });
        
    };

    return (
        <Container maxWidth="sm" sx={{m: 2}}>
        <Typography variant="h4" color="white" gutterBottom>
            Applicant Panel
        </Typography>

        {/* Request Reset */}
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2, mb: 4 }}>
            <TextField label="Email" variant="outlined" type="email" color="white" value={email} onChange={(e) => setEmail(e.target.value)} required />
            <Button variant="contained" color="primary" onClick={RequestReset}>Request Reset Link</Button>
        </Box>

        {/* Reset Password*/}
        <Box component="form" onSubmit={ResetPassword} sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
            <TextField label="Reset Token" variant="outlined" color="white" value={resetToken} onChange={(e) => setResetToken(e.target.value)} required />
            <TextField label="New Username" variant="outlined" color="white" value={username} onChange={(e) => setUsername(e.target.value)} required />
            <TextField label="New Password" variant="outlined" color="white" type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
            <Button type="submit" variant="contained" color="primary">Reset Password</Button>
        </Box>

        <Typography variant="h6" color="white">{message}</Typography>
        </Container>
    );
    };

    export default ApplicantForm;
