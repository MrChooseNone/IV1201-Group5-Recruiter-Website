import React from "react";

import Button from '@mui/material/Button';
import { Box, Card, CardContent, Typography } from "@mui/material";
import {Link} from 'react-router-dom';

export default function HomePageComp() {
    return (
      <Box
        sx={{
          bgcolor: "#8E8C8C",
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
          alignItems: "center",
          minHeight: "100vh",
          textAlign: "center",
          margin: 0,
          position: "relative", 
          zIndex: 0,
          overflow: "hidden"
        }}
      >
        
        {/* Middle Layer */}
        <Box
          sx={{
            bgcolor: "#FFFFFF", 
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            width: "100%",
            minHeight: "10vh",
            textAlign: "center",
            position: "absolute",
            zIndex: 1,
            top: 0,
            
            p: 4, 
          }}
        >
          <h1 style={{ color: "#2E3532", position: "relative", top: "-10px"}} >Application Website</h1>
        </Box>
  
        {/* Top Layer */}
        <Box
          sx={{
            bgcolor: "#8E8C8C",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
            width: "120%",
            height: 200,
            textAlign: "center",
            
            
            borderRadius: "50% 50% 0 0",
            position: "absolute",
            top: 100,
            zIndex: 2,
            
          }}
        >
          
        </Box>

        {/* Over Top Layer */}
        <Box
          sx={{
            bgcolor: "#8E8C8C",
            display: "flex", 
            justifyContent: "center", 
            alignItems: "center",
            gap: 3, 
            padding: 3, 
            flexDirection: {xs: "column", md: "row"},
            
            borderRadius: 4,
            position: "absolute",
            top: 150,
            zIndex: 3,
            
          }}
        >
          {/* Card 1 */}
            <Card sx={{
                bgcolor: "#AD8762", 
                width: 300, 
                height: 200, 
                p: 2, 
                textAlign: "center", 
                boxShadow: 3, 
                overflow: "hidden",
                
                }}>
                <CardContent>
                <Typography variant="h6">Step 1</Typography>
                <Typography variant="body2">Sign up!</Typography>
                </CardContent>
            </Card>

            {/* Card 2 */}
            <Card sx={{bgcolor: "#AD8762", width: 300, height: 200, p: 2, textAlign: "center", boxShadow: 3 }}>
                <CardContent>
                <Typography variant="h6">Step 2</Typography>
                <Typography variant="body2">Apply for the job!</Typography>
                <Button
                    sx={{
                    position: "relative",
                    backgroundColor: "#D3B08D", 
                    color: "#000",
                    fontWeight: "bold",
                    fontSize: "16px",
                    borderRadius: "4px",
                    padding: "8px 30px",
                    textTransform: "none",
                    boxShadow: "none",
                    cursor: "pointer",
                    overflow: "hidden",
                    marginTop: 11,
                    "&:hover": {
                        backgroundColor: "#B89B6D", 
                    },
                    "&:before": {
                        content: '""',
                        position: "absolute",
                        top: "-10px", 
                        left: "50%",
                        transform: "translateY(-10%) translateX(-50%) rotate(10deg)",
                        width: "120%",
                        height: "15px", 
                        backgroundColor: "#FFF1C9", 
                        borderRadius: "4px",
                        boxShadow: "0px 0px 10px rgba(0, 0, 0, 0.1)",
                    },
                    }}
                    >
                    <Typography variant="button" component={Link} to="/JobApplication" sx={{ color: "#000" }}>
                    Apply!
                    </Typography>
                </Button>
                </CardContent>
            </Card>
        </Box>
            
      </Box>
    );
}