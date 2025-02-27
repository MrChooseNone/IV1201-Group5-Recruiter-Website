import React, { useEffect, useState } from "react";
import DynamicBorderBox from "./BackgroudReactive";
import Box from "@mui/material/Box";

const Grid = ({rows = 5}) => {
    const [cols, setCols] = useState(5);

    useEffect(() => {
        const handleResize = () => {
        const newCols = Math.max(3, Math.floor(window.innerWidth / 10)); 
        setCols(newCols);
        };

        window.addEventListener("resize", handleResize);
        handleResize(); 

        return () => window.removeEventListener("resize", handleResize);
    }, []);

    return (
        <Box
        sx={{
            display: "grid",
            gridTemplateColumns: `repeat(${cols}, 1fr)`, 
            gap: 0,
            p: 0,
            m: 0,
            overflow: "hidden"
        }}
        >
        {Array.from({ length: cols * rows }).map((_, index) => (
            <DynamicBorderBox key={index} />
        ))}
        </Box>
    );
};

export default Grid;
