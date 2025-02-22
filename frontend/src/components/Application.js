import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {Link} from 'react-router-dom';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/

export default function ApplicationForm() {
    // UseState to save users inputed data and later send to database
    const[name,setName] = useState("")
    const[surname,setSurname] = useState("")
    const[email,setEmail] = useState("")
    const[username,setUsername] = useState("")
    const[personNumber,setPersonNumber] = useState("")
    const[password,setPassword] = useState("")
    // Handle the submition of data to database
    // Validation functions
  const validatePersonNumber = (pnr) => {
    return /^\d+$/.test(pnr); // Only numbers
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
        alert("Person number must contain only numbers");
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
      
      const url = "http://localhost:8080/person/register"
      
  
      console.log("Sending: ", applicant); // remove (just debug)
  
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
          console.log(data); // Write data
      })
      .catch((error) => {
          console.error("Error adding applicant:", error);
      });
  };
  
  return (
    <Box
      component="form"
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' }, 
          bgcolor: "#AFF9C9",
          display: "flex",
          flexDirection: {xs: "column", md: "row"},
          justifyContent: "center",
          alignItems: "center",
          gap: 3
      }}
      noValidate
      autoComplete="off"
    >
      <div>
        <TextField
          required
          id="standard-required"
          label="First name"
          
          variant="outlined"
          value={name}
          onChange={(e) => setName(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Surname"
          
          variant="outlined"
          value={surname}
          onChange={(e) => setSurname(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Person number"
          
          variant="outlined"
          value={personNumber}
          onChange={(e) => setPersonNumber(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="E-mail"
          type='email'
          variant="outlined"
          value={email}
          onChange={(e) => setEmail(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Username"
          
          variant="outlined"
          value={username}
          onChange={(e) => setUsername(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Password"
          type='password'
          variant="outlined"
          value={password}
          onChange={(e) => setPassword(e.target.value)} // Save inputed data on change
        />
        <Box sx={{
          bgcolor: "#AFF9C9",
          display: "flex",
          flexDirection: {xs: "column", md: "row"},
          justifyContent: "center",
          alignItems: "center",
          gap: 3
        }}>

          <Button variant="outlined" onClick={handleSubmit}>Submit</Button>
          <Button variant="outlined" component={Link} to="/JobApplication" >Next</Button>
        </Box>
      
      </div>
    </Box>
  );
}
