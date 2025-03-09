import Appbar from "../components/Appbar";
import * as React from 'react';
import Application from  "../components/Application"
import CssBaseline from '@mui/material/CssBaseline';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ApplicationEndPoint from "../components/ApplicationEndPoint";
import FollowMouse from "../components/FollowMouse";

import BlockNonApplicant from "../components/BlockNonApplicant"

export default function JobApplication(){
    return (
        <>
        <BlockNonApplicant></BlockNonApplicant>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <Container maxWidth="sm">
                <Box sx={{ height: '100%'}} >                     
                    <ApplicationEndPoint/>
                </Box>
            </Container>
        </>
       
    )
}