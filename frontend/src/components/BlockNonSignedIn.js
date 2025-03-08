import { AuthContext } from '../App';

import { useContext,useEffect } from 'react';

import { useNavigate } from "react-router-dom";

/**
 * This component forces any non-logged in users back to the home page
 * @returns The react function itself, with no html but a use effect which redirects non-logged in users
 */
export default function BlockNonSignedIn() {

    const navigate = useNavigate();
    const { auth, setAuth } = useContext(AuthContext);

    useEffect(() => {
        if (!auth.token) {
            navigate("/login");
        }
    }, []);

    return <></>

}