import Appbar from "../components/Appbar";
import * as React from 'react';
import Application from  "../components/Application"
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
            <Container maxWidth="sm">
                <Box sx={{  height: '100vh' }} >   
                    <Application/>  
                </Box>
            </Container>
        </>
    )
}