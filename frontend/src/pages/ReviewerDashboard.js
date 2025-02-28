import Appbar from "../components/Appbar";
import * as React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ReviewerEndPoints from "../components/ReviewerEndPoints";
import FollowMouse from "../components/FollowMouse";



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
        <ReviewerEndPoints></ReviewerEndPoints>
        </>
    )
}