import React, {useState, useEffect} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import List from '@mui/material/List';

/**
 * This component is responsible for handling showing the list of competence
 * @returns The component itself
 */
export default function Competences() {
    const[competences,setCompetences] = useState([]);

    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;

    const fetchCompetences = () => {
        const url = `${API_URL}/translation/getStandardCompetences`;

        console.log("Fetching competences from:", url);

        fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
            },
        })
        .then((response) => response.json()) // Parsar svaret som JSON
        .then((data) => {
            console.log("Received competences:", data);
            setCompetences(data); // Uppdaterar state med kompetenslistan
        })
        .catch((error) => {
            console.error("Error fetching competences:", error);
        });
    };

    // Hämta kompetenser automatiskt vid sidladdning
    useEffect(() => {
        fetchCompetences();
    }, []);
  return (
    <Box
      component="form"
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' }, 
      bgcolor: "#8E8C8C",
      display: "flex",
      flexDirection: "column",
      justifyContent: "center",
      alignItems: "center",}}
      noValidate
      autoComplete="off"
      
    >
        <Button variant='contained' onClick={fetchCompetences}> fetch competences</Button>
        {competences.length > 0 ? (
                    competences.map((competence, index) => (
                        <List key={index} className="border p-2 rounded-md mt-1">
                            {competence.name}
                        </List>
                    ))
                ) : (
                    <p>No competences.</p>
                )}
      
    </Box>
  );
}