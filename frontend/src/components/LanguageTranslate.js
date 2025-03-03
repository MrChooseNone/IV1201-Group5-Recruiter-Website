import React, { useState } from "react";
import {
  
  Typography,
  Button,
  TextField,
  List,
  ListItem,
  ListItemText,
  Divider,
} from "@mui/material";

export default function LanguageTranslate(){
    const [language, setLanguage] = useState("");
    const [translations, setTranslations] = useState([]);
    const [languages, setLanguages] = useState([]);

    // Get API URL from .env file
    const API_URL = process.env.REACT_APP_API_URL;
    
    const fetchLanguages = async () => {
        try {
        const response = await fetch(`${API_URL}/translation/getLanguages`);
        if (!response.ok) throw new Error("Failed to fetch languages");
        const data = await response.json();
        setLanguages(data);
        } catch (error) {
        console.error(error);
        }
    };

    const fetchCompetenceTranslations = async () => {
        if (!language) return;
        console.log("language is set");
        try {
            const url = `${API_URL}/translation/getCompetenceTranslation?language=${language}`;
            fetch(url, {
                method: "GET",
                headers: {
                    // Send as form data, to comply with us using @Requestparam in controller
                    "Content-Type": "application/x-www-form-urlencoded", 
                },
            })
            .then((response) => response.json()) // Parse response as text
            .then((data) => {
                console.log(data); // Write data
                setTranslations(data);
            })
            .catch((error) => {
                console.error("Error adding applicant:", error);
            });
        
        } catch (error) {
            console.error(error);
            setTranslations([]);
        }
    };
    return(
        <>
            <Typography variant="h6" style={{ marginTop: "20px" }}>Get Competence Translations</Typography>
            <TextField
                label="Language name"
                variant="outlined"
                fullWidth
                margin="dense"
                value={language}
                onChange={(e) => setLanguage(e.target.value)}
            />
            <Button variant="contained" color="primary" onClick={fetchCompetenceTranslations}>
                Fetch Translations
            </Button>
            <List>
                {translations.map((t) => (
                <ListItem key={t.competenceTranslationId}>
                    <ListItemText primary={t.translation} secondary={`Competence in english: ${t.competence.name}`} />
                </ListItem>
                ))}
            </List>
            <Divider />

            
            <Typography variant="h6" style={{ marginTop: "20px" }}>Supported Languages</Typography>
            <Button variant="contained" color="primary" onClick={fetchLanguages}>
                Fetch Languages
            </Button>
            <List>
                {languages.map((l) => (
                <ListItem key={l.languageId}>
                    <ListItemText primary={l.languageId} secondary={`Language: ${l.languageName}`} />
                </ListItem>
                ))}
            </List>
        </>
    )
}