import Appbar from "../components/Appbar";
import * as React from 'react';

import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import FollowMouse from "../components/FollowMouse";
import RecruiterForm from  "../components/UpdateAccountRecruiter"
import BlockNonReviewers from "../components/BlockNonRecruiter";



export default function UpdateAccountRecruiterPage(){
    return (
        <>
        <BlockNonReviewers></BlockNonReviewers>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <Container maxWidth="sm">
                <Box sx={{  height: '100vh' }} >   
                    <RecruiterForm/>  
                </Box>
            </Container>
        </>
    )
}