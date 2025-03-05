import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {Link} from 'react-router-dom';
import { Typography, Stack, Container } from '@mui/material';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/

export default function LoginComp() {
  // UseState to save users inputed data and later send to database
  const[username,setUsername] = useState("");
  const[password,setPassword] = useState("");
 

  // Get API URL from .env file
  const API_URL = process.env.REACT_APP_API_URL;

  // Handle the submition of data to database
  // Validation functions

  const validatePassword = (password) => {
    // Example: Password must be at least 8 characters
    return password.length >= 8;
  };

  // Handle form submission
  const handleLogin = (e) => {
      e.preventDefault(); // Prevents page refresh

      // Validate field
      if (!validatePassword(password)) {
        alert("Password must be at least 8 characters");
        return;
      }
      const params = new URLSearchParams();
      params.append("username", username);
      params.append("password", password);
      
      
      const url = `${API_URL}/auth/generateToken?${params.toString()}`;
      
      console.log("Sending: ", url); // remove (just debug)
      

        fetch(url, {
            method: "POST",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
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
            console.log("data: " + data); // Write data
            localStorage.setItem("token", data);
        })
        .catch((error) => {
            console.error("Error adding applicant:", error);
            alert("Username or password is incorrect")
        });
      
  };
  
  return (
      <Box
      component="form"
      sx={{
        bgcolor: "#AFF9C9",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        p: 4,
        borderRadius: 2,
        }}
        noValidate
        autoComplete="off"
      >
        {/* Title Section */}
        <Box sx={{ textAlign: "center", mb: 3 }}>
          <Typography variant="h5" fontWeight="bold">
            Login!
          </Typography>
        </Box>

        {/* Input Fields */}
        <Stack spacing={2} sx={{ width: "100%"}}>
          
          <TextField required label="Username" variant="outlined" value={username} onChange={(e) => setUsername(e.target.value)} fullWidth />
          <TextField required label="Password" type="password" variant="outlined" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth />
        </Stack>

        {/* Buttons Section */}
        <Stack direction={{ xs: "column", md: "row" }} spacing={2} mt={3}>
          <Button variant="contained" color="primary" onClick={handleLogin}>
            Login
          </Button>
          
        </Stack>
        <Typography>{localStorage.getItem("token")}</Typography>
      </Box>

  );
}
