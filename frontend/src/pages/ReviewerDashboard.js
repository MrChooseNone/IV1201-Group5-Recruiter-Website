import Appbar from "../components/Appbar";
import * as React from 'react';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import ReviewerEndPoints from "../components/ReviewerEndPoints";
import FollowMouse from "../components/FollowMouse";
import SearchApplication from "../components/SearchApplication";
import SearchByUsername from "../components/SearchByUsername";
import BlockNonReviewers from "../components/BlockNonRecruiter";


export default function ReviewerDashboard() {

    return (
        <>
            <BlockNonReviewers/>
            <Appbar />
            <FollowMouse></FollowMouse>
            <Box sx={{
                display: "flex", flexDirection: { xs: "column", md: "row" }, gap: 2, alignItems: { xs: "center", md: "flex-start" }, bgcolor: "#AFF9C9", width: "95%",
                height: "100%",
                justifySelf: "center",
                p: 2,
                borderRadius: 4,
                marginTop: 2,
                overflow: "hidden"
            }}>
                <ReviewerEndPoints></ReviewerEndPoints>
                <SearchApplication></SearchApplication>
                <SearchByUsername></SearchByUsername>
            </Box>
        </>
    )
}