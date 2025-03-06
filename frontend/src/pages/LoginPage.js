import Appbar from "../components/Appbar";
import * as React from 'react';
import LoginComp from  "../components/LoginComp"
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import FollowMouse from "../components/FollowMouse";



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <Container maxWidth="sm">
                <Box sx={{  height: '100vh' }} >   
                    <LoginComp/>  
                </Box>
            </Container>
        </>
    )
}