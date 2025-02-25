import Appbar from "../components/Appbar";
import * as React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ReviewerEndPoints from "../components/ReviewerEndPoints";



export default function AddApplicant(){
    return (
        <>
        <Appbar/>
            <ReviewerEndPoints></ReviewerEndPoints>
        </>
    )
}