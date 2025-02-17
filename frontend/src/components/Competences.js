
import React, {useState, useEffect} from 'react';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Button from '@mui/material/Button';
import List from '@mui/material/List';

export default function Competences() {
    const[competences,setCompetences] = useState([])

    const fetchCompetences = () => {
        const url = "http://localhost:8080/translation/getStandardCompetences";

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
      sx={{ '& .MuiTextField-root': { m: 1, width: '25ch' } }}
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