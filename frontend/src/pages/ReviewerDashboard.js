import Appbar from "../components/Appbar";
import * as React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ReviewerEndPoints from "../components/ReviewerEndPoints";
import FollowMouse from "../components/FollowMouse";
import SearchApplication from "../components/SearchApplication";
import SearchByUsername from "../components/SearchByUsername";



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
        <Box sx={{display: "flex", flexDirection: {xs: "column", md: "row"}, gap: 1, alignItems: "center"}}>
            <ReviewerEndPoints></ReviewerEndPoints>
            <SearchApplication></SearchApplication>
            <SearchByUsername></SearchByUsername>
        </Box>
        </>
    )
}