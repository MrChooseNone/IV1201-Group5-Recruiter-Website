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
    
    const fetchLanguages = async () => {
        try {
        const response = await fetch(`http://localhost:3000/translation/getLanguages`);
        if (!response.ok) throw new Error("Failed to fetch languages");
        const data = await response.json();
        setLanguages(data);
        } catch (error) {
        console.error(error);
        }
    };

    const fetchCompetenceTranslations = async () => {
        if (!language) return;
        try {
            const url = `http://localhost:3000/translation/getCompetenceTranslation?language=${language}`;
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
                label="Language Code (e.g., 'en', 'fr')"
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
                <ListItem key={t.id}>
                    <ListItemText primary={t.translation} secondary={`Competence ID: ${t.competenceId}`} />
                </ListItem>
                ))}
            </List>
            <Divider />

            
            <Typography variant="h6" style={{ marginTop: "20px" }}>Supported Languages</Typography>
            <Button variant="contained" color="primary" onClick={fetchLanguages}>
                Fetch Languages
            </Button>
            <List>
                {languages.map((lang) => (
                <ListItem key={lang.code}>
                    <ListItemText primary={lang.name} secondary={`Code: ${lang.code}`} />
                </ListItem>
                ))}
            </List>
        </>
    )
}