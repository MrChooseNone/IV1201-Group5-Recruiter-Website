import { useState } from "react";
import Box from '@mui/material/Box';

/**
 * This component is responsible for the reactive background 
 * @returns The component itself
 */
export default function DynamicBorderBox() {
    const [borderRadius, setBorderRadius] = useState("10px");

    /**
     * This tracks the mouse, and updates the reactive element correctly
     * @param {*} e The event, aka mouse movement, which caused this to be triggered
     */
    const handleMouseMove = (e) => {
        const { offsetX, offsetY, target } = e.nativeEvent;
        const { clientWidth, clientHeight } = target;

        // Normalize values (0 to 100)
        const xPercent = (offsetX / clientWidth) * 100;
        const yPercent = (offsetY / clientHeight) * 100;

        // Compute border-radius values based on cursor position
        const topLeft = Math.max(50 - xPercent, 50 - yPercent);
        const topRight = Math.max(xPercent, 50 - yPercent);
        const bottomLeft = Math.max(50 - xPercent, yPercent);
        const bottomRight = Math.max(xPercent, yPercent);

        setBorderRadius(`${topLeft}% ${topRight}% ${bottomRight}% ${bottomLeft}%`);
    };

    return (
        <Box
        
        sx={{
            width: "20px",
            height: "20px",
            background: "#67E0A3",
            borderRadius: borderRadius,
            transition: "border-radius 0.2s ease-out",
        }}
        onMouseMove={handleMouseMove}
        
        
        ></Box>
    );
}
