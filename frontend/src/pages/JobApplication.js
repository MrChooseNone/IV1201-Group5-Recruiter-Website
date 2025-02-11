import Appbar from "../components/Appbar";
import * as React from 'react';
import Application from  "../components/Application"
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';


export default function JobApplication(){
    return (
        <>
        <Appbar/>
            <React.Fragment>
                <CssBaseline />
                <Container maxWidth="sm">
                    <Box sx={{ bgcolor: '#cfe8fc', height: '100vh' }} >

                        
                        <Application/>  
                    </Box>
                </Container>
            </React.Fragment>
        </>
       
    )
}