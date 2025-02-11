
import React, {useState} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';

export default function SearchApplication() {
    const[search,setSearch] = useState("")
    const[result,setResult] = useState([])

    const handleSearch = (e) => {
        e.preventDefault(); // Prevents page refresh
        const param = new URLSearchParams({name: search});
        const url = new URL("http://localhost:8080/person/find");
        

        // Add the params to the URL
        url.search = param.toString();
    
        console.log("Sending request with params:", url); // remove (just debug)
    
        fetch(url, {
            method: "GET",
            headers: {
                // Send as form data, to comply with us using @Requestparam in controller
                "Content-Type": "application/x-www-form-urlencoded", 
            },
        })
        .then((response) => response.text()) // Parse response as text
        .then((data) => {
            console.log(data); // Write data
            setResult(data)
        })
        .catch((error) => {
            console.error("Error Searching for:", error);
        });
    
    }
  return (
    <Box
      component="form"
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' } }}
      noValidate
      autoComplete="off"
      onSubmit={handleSearch} // Allows Enter key to trigger search
    >
      <div>
       
        <TextField
          required
          id="standard-required"
          label="Search"
          
          variant="standard"
          value={search}
          onChange={(e) => setSearch(e.target.value)} // Save inputed data on change
        />
        <Button variant="outlined" onClick={handleSearch}>Submit</Button>
        <h3>{result}</h3>
      </div>
    </Box>
  );
}
