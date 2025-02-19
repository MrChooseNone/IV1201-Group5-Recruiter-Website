import React, { useState, useEffect } from "react";
import {
  Container,
  Typography,
  Button,
  TextField,
  List,
  ListItem,
  ListItemText,
  Divider,
} from "@mui/material";

export default function CompetencesByID(){

    const [competenceId, setCompetenceId] = useState("");
    const [specificCompetence, setSpecificCompetence] = useState(null);
    
    const fetchSpecificCompetence = async () => {
        if (!competenceId) return;
        try {
          const response = await fetch(`http://localhost:8080/translation/getSpecificCompetence/${competenceId}`);
          if (!response.ok) throw new Error("Competence not found");
          const data = await response.json();
          setSpecificCompetence(data);
        } catch (error) {
          console.error(error);
          setSpecificCompetence(null);
        }
    };

    return (
        <>
            <Typography variant="h6" style={{ marginTop: "20px" }}>Get Specific Competence</Typography>
            <TextField
                label="Competence ID"
                variant="outlined"
                fullWidth
                margin="dense"
                value={competenceId}
                onChange={(e) => setCompetenceId(e.target.value)}
            />
            <Button variant="contained" color="primary" onClick={fetchSpecificCompetence}>
                Fetch Competence
            </Button>
            {specificCompetence && (
                <Typography style={{ marginTop: "10px" }}>
                <strong>ID:</strong> {specificCompetence.competenceId} <br />
                <strong>Name:</strong> {specificCompetence.name}
                </Typography>
            )}
            <Divider />
        </>
    )
}