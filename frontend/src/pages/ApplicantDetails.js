
import Appbar from "../components/Appbar";
import * as React from 'react';
import ApplicantDetailsComp from "../components/ApplicantDetailsComp";
import FollowMouse from "../components/FollowMouse";
import BlockNonReviewers from "../components/BlockNonRecruiter";


export default function AddApplicant(){

    return (
        <>
        <BlockNonReviewers/>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <ApplicantDetailsComp></ApplicantDetailsComp>
        </>
    )
}