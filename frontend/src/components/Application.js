import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {Link} from 'react-router-dom';
import { Typography, Stack, Container } from '@mui/material';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/

/**
 * This is the component responsible for handling registration of a new applicant
 * @returns The component
 */
export default function ApplicationForm() {
  // UseState to save users inputed data and later send to database
  const[name,setName] = useState("");
  const[surname,setSurname] = useState("");
  const[email,setEmail] = useState("");
  const[username,setUsername] = useState("");
  const[personNumber,setPersonNumber] = useState("");
  const[password,setPassword] = useState("");
  const[isSubmited, setIsSubmited] = useState(false);

  // Get API URL from .env file
  const API_URL = process.env.REACT_APP_API_URL;

  /**
   * This function checks if a person number is valid or not
   * @param {*} pnr the pnr to check if it is valid or not  
   * @returns A boolean for if this is a valid pnr or not
   */
  const validatePersonNumber = (pnr) => {
    return /\d{8}-\d{4}/.test(pnr); 
  };

  /**
   * This function checks if an email is valid or not
   * @param {*} email the email to check if it is valid or not  
   * @returns A boolean for if this is a valid email or not
   */
  const validateEmail = (email) => {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(email);
  };

    /**
   * This function checks if a password is valid or not
   * @param {*} email the password to check if it is valid or not  
   * @returns A boolean for if this is a valid password or not
   */
  const validatePassword = (password) => {
    // Example: Password must be at least 8 characters
    return password.length >= 8;
  };

  /**
   * This function handles submission of the registration form, 
   * first validating the values in the frontend and then sending an application, 
   * and giving an alert to the user about if it worked or not (and if not why)
   * @param {T} e The even which caused this function to be called
   */
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
        .then((response) => {
          
          if(response.ok)
          {
             response.text()
          }
          else
          {
            return response.text().then((errorText) => { 
              throw new Error(`${errorText}`); 
            });
          }
         
        
        }) // Parse response as text
        .then((data) => {
            console.log("data: " + data); // Write data
            setIsSubmited(true);
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
            alert(error)
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
        marginTop: 2,
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
          <Button disabled={!isSubmited} variant="contained" color="secondary" component={Link} to="/login">
            Next →
          </Button>
        </Stack>
      </Box>

  );
}
