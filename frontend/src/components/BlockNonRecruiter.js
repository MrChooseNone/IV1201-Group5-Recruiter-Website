import { AuthContext } from '../App';

import { useContext,useEffect } from 'react';

import { useNavigate } from "react-router-dom";

/**
 * This component forces any non-recruiter users back to the home page
 * @returns The react function itself, with no html but a use effect which redirects non-recruiters
 */
export default function BlockNonReviewers() {

    const navigate = useNavigate();
    const { auth, setAuth } = useContext(AuthContext);

    /**
     * This is called once the component is loaded, and checks if the user is an recruiter, if not they are sent to the login page
     */
    useEffect(() => {
        if (auth.role !== "recruiter") {
            navigate("/login");
        }
        else {
            console.log("Welcome Recruiter");
        }
    }, []);

    return <></>

}