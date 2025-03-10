import Appbar from "../components/Appbar";
import * as React from 'react';

import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import FollowMouse from "../components/FollowMouse";
import ApplicantForm from  "../components/UpdateAccountApplicant"



export default function UpdateAccountPage(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <Container maxWidth="sm">
                <Box sx={{  height: '100vh' }} >   
                    <ApplicantForm/>  
                </Box>
            </Container>
        </>
    )
}