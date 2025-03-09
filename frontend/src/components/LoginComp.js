import React, {useState,useContext, useEffect} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import {Link, useNavigate} from 'react-router-dom';
import { Typography, Stack, Container } from '@mui/material';

import { AuthContext } from '../App';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/


export default function LoginComp() {
  // UseState to save users inputed data and later send to database
  const[username,setUsername] = useState("");
  const[password,setPassword] = useState("");
  const[search,setSearch] = useState("");
  const[result,setResult] = useState(null);
  const[isSubmited, setIsSubmited] = useState(false);

  const navigate = useNavigate();

  const {auth,setAuth} = useContext(AuthContext);

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

      if(username.length==0) //This checks that a username was submitted, if not it notifies the user and returns before sending the request
      {
        alert("Username must not be blank");
        return;
      }

      // Validate field, add this back in after testing!
      /*
      if (!validatePassword(password)) {
        alert("Password must be at least 8 characters");
        return;
      }* */

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

              if(response.status==401) //This checks if the credentials are incorrect, and if so gives a correct error message
              {
                return response.text().then((errorText) => { 
                  throw new Error(`${errorText}`); 
              });
              }
              else{
              
                return response.text().then((errorText) => { 
                    throw new Error(`Failed to fetch: ${errorText}`); 
                });
              }
            }
          }) 
        .then((data) => {
            //We first parse the json we recive, then set the current auth information, and then store it in case the user refreshes the page
            //Data is in the format {"token":<token>,"role":<role>}
            const dataParsed = JSON.parse(data);
            setAuth(dataParsed); 
            console.log(dataParsed.token)
            console.log(dataParsed.id)
            console.log(data)

            //TODO update this to not use local storage
            sessionStorage.setItem("token", dataParsed.token);
            sessionStorage.setItem("role", dataParsed.role);
            sessionStorage.setItem("id", dataParsed.id);
            setIsSubmited(true);
            alert("Login Successful");
            loginNavigate();

        })
        .catch((error) => {
            console.error("Error adding applicant:", error);
            alert(error);
        });
      
   };

    const parseSearch = () => {
        if(/^\d+-?\d+$/.test(search)) return "pnr";
        if(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/.test(search)) return "email";
        else return "username";
    };

    //navigate to /JobApplication or /review based on the role of the logged in
    const loginNavigate = () =>{
        if(sessionStorage.getItem("role") === "applicant"){
            navigate("/JobApplication");
        } else {
            navigate("/review");
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
            Login →
          </Button>
          
        </Stack>
        
      </Box>

      

  );
}
