
import Appbar from "../components/Appbar";
import * as React from 'react';
import ApplicantDetailsComp from "../components/ApplicantDetailsComp";
import FollowMouse from "../components/FollowMouse";

export default function AddApplicant(){
    return (
        <>
        <Appbar/>
        <FollowMouse></FollowMouse>
            <ApplicantDetailsComp></ApplicantDetailsComp>
        </>
    )
}