import React from "react";

import Button from '@mui/material/Button';
import { Box, Card, CardContent, Typography } from "@mui/material";
import {Link} from 'react-router-dom';
import LoginIcon from '@mui/icons-material/Login';
import Grid from "./Grid";

/**
 * Home page component displaying the steps for user actions with cards
 */
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
            gap: 2, 
            padding: {xs: 8, md: 0}, 
            flexDirection: {xs: "column", md: "row"},
            width: "100%",
            height: "100vh",
            borderRadius: 4,
            position: "relative",
            top: 150,
            zIndex: 3,
            marginTop: {xs: 2, md: 0},
            marginBottom: {xs: 15, md: 1},
            
          }}
        >
          <Typography variant="h4" sx={{ color: "white", position: "absolute", top: 0}} >Theme park application</Typography>
        
          {/* Card 1 */}
            <Card sx={{
                bgcolor: "#67E0A3", 
                width: 300, 
                height: 200, 
                p: 2, 
                top: 0,
                textAlign: "center", 
                boxShadow: 3, 
                overflow: "hidden",
                
                }}>
                <CardContent>
                <Typography variant="h4">Step 1</Typography>
                <Typography variant="h6">Register as an applicant!</Typography>
                <Button variant="contained" component={Link} to="/addApplicant" sx={{bgcolor: "#18624B", m: 2}}>
                    <Typography>
                    Register Now!
                    </Typography>
                    
                </Button>

                </CardContent>
            </Card>

            {/* Card 2 */}
            <Card sx={{bgcolor: "#67E0A3", width: 300, height: 200, p: 2, textAlign: "center", boxShadow: 3 }}>
                <CardContent>
                <Typography variant="h4">Step 2</Typography>
                <Typography variant="h6">Login to your account!</Typography>
                <Button variant="contained" component={Link} to="/login" sx={{bgcolor: "#18624B", m: 2}}>
                    <Typography>
                    Login!
                    </Typography>
                    <LoginIcon fontSize="large"></LoginIcon>
                    </Button>
                </CardContent>
            </Card>

            {/* Card 3 */}
            <Card sx={{bgcolor: "#67E0A3", width: 300, height: 200, p: 2, textAlign: "center", boxShadow: 3 }}>
                <CardContent>
                <Typography variant="h4">Step 3</Typography>
                <Typography variant="h6">Apply for the job!</Typography>
                <Button variant="contained" component={Link} to="/JobApplication" sx={{bgcolor: "#18624B", m: 2}}>
                    <Typography>
                    Apply for the job!
                    </Typography>
                </Button>
                </CardContent>
            </Card>
        </Box>
            
      </Box>
    );
}