import React, {useState , useEffect} from "react";
import { Box } from "@mui/material";

/**
 * FollowMouse component, which tracks and displays a small circle following the mouse position
 */
export default function FollowMouse() {
    const [pos, setPos] = useState("");
    useEffect(() => {
        const handleFollow = (e) => {
            const {clientX, clientY} = e;
    
            setPos(`${clientX -0}px, ${clientY -83}px`);
        }

        window.addEventListener("mousemove", handleFollow);

        return () => window.removeEventListener("mousemove", handleFollow)
    }, [])

    return(  
        
        <Box sx={{
            bgcolor: "#D33F49",
            width: "20px",
            height: "20px",
            borderRadius: "50% 50% 50% 10%",
            transform:  `translate(${pos})`,
            transition: "transform 0.3s ease-out",
            zIndex: 9999,
            position: "fixed",
            pointerEvents: "none",
            
        }}>
            
        </Box>
    );
};