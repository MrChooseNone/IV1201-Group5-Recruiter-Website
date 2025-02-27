import Appbar from "../components/Appbar";
import * as React from 'react';
import Application from  "../components/Application"
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ApplicationEndPoint from "../components/ApplicationEndPoint";
import FollowMouse from "../components/FollowMouse";


export default function JobApplication(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <Container maxWidth="sm">
                <Box sx={{ height: '100%', p: 2 }} >                     
                    <ApplicationEndPoint/>
                </Box>
            </Container>
        </>
       
    )
}