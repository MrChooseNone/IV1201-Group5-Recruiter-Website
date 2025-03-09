import {jwtDecode} from "jwt-decode";

export const isTokenExpired = (token) => {
    if (!token) return true; // If no token, consider it expired
    try {
        const { exp } = jwtDecode(token);

        const currentTime = Math.floor(Date.now() / 1000); // Get time in seconds
        return exp < currentTime; // return true if expired
    } catch (error) {
        return true; // If there's an error treat as expired
    }
};
