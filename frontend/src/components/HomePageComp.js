import React from "react";

import Button from '@mui/material/Button';
import { Box, Card, CardContent, Typography } from "@mui/material";
import {Link} from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';
import Grid from "./Grid";
import { ThemeContext } from "@emotion/react";

export default function HomePageComp() {
    return (
      <Box
        sx={{
          bgcolor: "#18624B",
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
            bgcolor: "#18624B", 
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
            
            p: 0, 
          }}
        >
          <Grid rows={10}>

          </Grid>
        </Box>
  
        {/* Top Layer */}
        <Box
          sx={{
            bgcolor: "#18624B",
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
            bgcolor: "#18624B",
            display: "flex", 
            justifyContent: "center", 
            alignItems: "center",
            gap: 3, 
            padding: 8, 
            flexDirection: {xs: "column", md: "row"},
            
            borderRadius: 4,
            position: "absolute",
            top: 150,
            zIndex: 3,
            
          }}
        >
          <Typography variant="h4" sx={{ color: "white", position: "absolute", top: 0}} >Theme park application</Typography>
        
          {/* Card 1 */}
            <Card sx={{
                bgcolor: "#67E0A3", 
                width: 300, 
                height: 200, 
                p: 2, 
                textAlign: "center", 
                boxShadow: 3, 
                overflow: "hidden",
                
                }}>
                <CardContent>
                <Typography variant="h4">Step 1</Typography>
                <Typography variant="h6">Sign up!</Typography>
                <LoginIcon fontSize="large"></LoginIcon>
                </CardContent>
            </Card>

            {/* Card 3 */}
            <Card sx={{bgcolor: "#67E0A3", width: 300, height: 200, p: 2, textAlign: "center", boxShadow: 3 }}>
                <CardContent>
                <Typography variant="h4">Step 2</Typography>
                <Typography variant="h6">Register as a applicant!</Typography>
                
                </CardContent>
            </Card>

            {/* Card 2 */}
            <Card sx={{bgcolor: "#67E0A3", width: 300, height: 200, p: 2, textAlign: "center", boxShadow: 3 }}>
                <CardContent>
                <Typography variant="h4">Step 3</Typography>
                <Typography variant="h6">Apply for the job!</Typography>
                <Button
                    sx={{
                    position: "relative",
                    backgroundColor: "#AFF9C9", 
                    color: "#000",
                    fontWeight: "bold",
                    fontSize: "16px",
                    borderRadius: "4px",
                    padding: "8px 30px",
                    textTransform: "none",
                    boxShadow: "none",
                    cursor: "pointer",
                    overflow: "hidden",
                    m: 2,
                    "&:hover": {
                        backgroundColor: "#7CF0BD", 
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
                    <Typography variant="button" component={Link} to="/addApplicant" sx={{ color: "#000" }}>
                    Register Now!
                    </Typography>
                </Button>
                </CardContent>
            </Card>
        </Box>
            
      </Box>
    );
}