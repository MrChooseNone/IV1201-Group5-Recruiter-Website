import {jwtDecode} from "jwt-decode";
import { useNavigate } from "react-router-dom";


/**
 * This function is responsible for checking and handling if a token is expired
 * @param {*} token The token to verify
 * @returns True if token expired, non-existent or invalid, false if it is not expired and valid as a jwt token
 */
export const isTokenExpired = (token) => {
    const navigate = useNavigate();

    if (token) // Check if there is a token
    {
        try {
        const { exp } = jwtDecode(token);

        const currentTime = Math.floor(Date.now() / 1000); // Get time in seconds

        if(!(exp < currentTime))
        {
            return false; //if expiration date >= currentTime token is not expired, and return false
        }
    } 
        catch (error) {}
    }
    // If there was an error, no token or it was expired then remove authentication from state + session storage, alert that token was expired + naviage to login
    setAuth({});
    sessionStorage.clear();
    alert("Your session has expired. Please log in again.");
    navigate("/login"); // Redirect to login page
    return true;
};
