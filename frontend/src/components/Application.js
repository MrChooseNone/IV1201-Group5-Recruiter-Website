import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

// Used "Material UI" as reference https://mui.com/material-ui/react-text-field/

export default function ApplicationForm() {
    // UseState to save users inputed data and later send to database
    const[name,setName] = useState("")
    const[surname,setSurname] = useState("")
    // const[email,setEmail] = useState("")
    // const[number,setNumber] = useState("")
    // Handle the submition of data to database
    const handleSubmit = (e) => {
      e.preventDefault(); // Prevents page refresh
      const applicant = { name, surname }; // Prepare the data to be sent
      const url = new URL("http://localhost:8080/person/add");
      const params = new URLSearchParams(applicant);
  
      // Add the params to the URL
      url.search = params.toString();
  
      console.log("Sending request with params:", url); // remove (just debug)
  
      fetch(url, {
          method: "POST",
          headers: {
              // Send as form data, to comply with us using @Requestparam in controller
              "Content-Type": "application/x-www-form-urlencoded", 
          },
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
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' } }}
      noValidate
      autoComplete="off"
    >
      <div>
        <TextField
          required
          id="standard-required"
          label="Required"
          defaultValue="First name"
          variant="standard"
          value={name}
          onChange={(e) => setName(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Required"
          defaultValue="Surname"
          variant="standard"
          value={surname}
          onChange={(e) => setSurname(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Required"
          defaultValue="E-mail"
          variant="standard"
          // value={email}
          // onChange={(e) => setEmail(e.target.value)} // Save inputed data on change
        />
        <TextField
          required
          id="standard-required"
          label="Phone number"
          defaultValue=""
          variant="standard"
          // value={number}
          // onChange={(e) => setNumber(e.target.value)} // Save inputed data on change
        />
        <Button variant="outlined" onClick={handleSubmit}>Submit</Button>
      
      </div>
    </Box>
  );
}
