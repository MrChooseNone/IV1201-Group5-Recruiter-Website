import Appbar from "../components/Appbar";
import * as React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ReviewerEndPoints from "../components/ReviewerEndPoints";
import FollowMouse from "../components/FollowMouse";
import SearchApplication from "../components/SearchApplication";



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
        <Box sx={{display: "flex", flexDirection: "row", gap: 1}}>
            <ReviewerEndPoints></ReviewerEndPoints>
            <SearchApplication></SearchApplication>
        </Box>
        </>
    )
}