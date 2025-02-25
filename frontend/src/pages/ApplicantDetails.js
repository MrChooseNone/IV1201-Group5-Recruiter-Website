
import Appbar from "../components/Appbar";
import * as React from 'react';
import ApplicantDetailsComp from "../components/ApplicantDetailsComp";

export default function AddApplicant(){
    return (
        <>
        <Appbar/>
            <ApplicantDetailsComp></ApplicantDetailsComp>
        </>
    )
}