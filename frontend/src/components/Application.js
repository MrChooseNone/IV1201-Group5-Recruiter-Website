import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {Link} from 'react-router-dom';
import { Typography, Stack, Container } from '@mui/material';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/

export default function ApplicationForm() {
  // UseState to save users inputed data and later send to database
  const[name,setName] = useState("");
  const[surname,setSurname] = useState("");
  const[email,setEmail] = useState("");
  const[username,setUsername] = useState("");
  const[personNumber,setPersonNumber] = useState("");
  const[password,setPassword] = useState("");

  // Get API URL from .env file
  const API_URL = process.env.REACT_APP_API_URL;

  // Handle the submition of data to database
  // Validation functions
  const validatePersonNumber = (pnr) => {
    return /^\d+-?\d+$/.test(pnr);
  };

  const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  };

  const validatePassword = (password) => {
    // Example: Password must be at least 8 characters
    return password.length >= 8;
  };

  // Handle form submission
  const handleSubmit = (e) => {
      e.preventDefault(); // Prevents page refresh

      // Validate fields
      if (!validatePersonNumber(personNumber)) {
        alert("Person number must follow this format: yyyymmdd-xxxx");
        return;
      }

      if (!validateEmail(email)) {
        alert("Please enter a valid email address");
        return;
      }

      if (!validatePassword(password)) {
        alert("Password must be at least 8 characters");
        return;
      }

      const applicant = { 
        name: name, 
        surname: surname, 
        pnr: personNumber, 
        email: email, 
        username: username,
        password: password
       }; // Prepare the data to be sent
      
      const url = `${API_URL}/person/register`;
      
  
      console.log("Sending: ", applicant); // remove (just debug)
      if(applicant.name && applicant.surname && applicant.username){

        fetch(url, {
            method: "POST",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
                "Content-Type": "application/json", 
            },
            body: JSON.stringify(applicant),
        })
        .then((response) => response.text()) // Parse response as text
        .then((data) => {
            console.log("data: " + data); // Write data
            if(data === "User registered successfully!"){
              alert(`Welcome ${applicant.name}!`);
            } else if (data === "PNR is already in use!"){
              alert(`Person number is already in use!`);
            } else if (data === "Email is already registered!"){
              alert(`Email is already registered!`);
            } else if (data === "Username is already taken!"){
              alert(`Username is already taken!`);
            }
        })
        .catch((error) => {
            console.error("Error adding applicant:", error);
            alert("Something went wrong, try again later")
        });
      } else{
        alert("Please fill in all the information!");
      }
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
            Create Your Account & Join the Fun!
          </Typography>
          <Typography variant="h6">
            Sign up to start your application.
          </Typography>
        </Box>

        {/* Input Fields */}
        <Stack spacing={2} sx={{ width: "100%"}}>
          <TextField required label="First Name" variant="outlined" value={name} onChange={(e) => setName(e.target.value)} fullWidth />
          <TextField required label="Surname" variant="outlined" value={surname} onChange={(e) => setSurname(e.target.value)} fullWidth />
          <TextField required label="Person Number" placeholder='YYYYMMDD-XXXX' variant="outlined" value={personNumber} onChange={(e) => setPersonNumber(e.target.value)} fullWidth />
          <TextField required label="E-mail" type="email" variant="outlined" value={email} onChange={(e) => setEmail(e.target.value)} fullWidth />
          <TextField required label="Username" variant="outlined" value={username} onChange={(e) => setUsername(e.target.value)} fullWidth />
          <TextField required label="Password" type="password" variant="outlined" value={password} onChange={(e) => setPassword(e.target.value)} fullWidth />
        </Stack>

        {/* Buttons Section */}
        <Stack direction={{ xs: "column", md: "row" }} spacing={2} mt={3}>
          <Button variant="contained" color="primary" onClick={handleSubmit}>
            Submit
          </Button>
          <Button variant="contained" color="secondary" component={Link} to="/JobApplication">
            Next →
          </Button>
        </Stack>
      </Box>

  );
}
