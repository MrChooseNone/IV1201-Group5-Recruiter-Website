import { AuthContext } from '../App';

import { useContext,useEffect } from 'react';

import { useNavigate } from "react-router-dom";

/**
 * This component forces any non-logged in users back to the home page
 * @returns The react function itself, with no html but a use effect which redirects non-logged in users
 */
export default function BlockNonApplicant() {

    const navigate = useNavigate();
    const { auth, setAuth } = useContext(AuthContext);

    /**
     * This is called once the component is loaded, and checks if the user is an applicant, if not they are sent to the login page
     */
    useEffect(() => {
        if (auth.role !== "applicant") {
            navigate("/login");
        }
    }, []);

    return <></>

}