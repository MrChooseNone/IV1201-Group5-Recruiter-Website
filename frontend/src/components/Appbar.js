import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import {Link} from 'react-router-dom';

export default function Appbar() {
  return (
      <AppBar position="sticky">
        <Toolbar sx={{
            display: "flex",
            flexWrap: "wrap", // Prevents text overlap
            justifyContent: "space-between",
            alignItems: "center",
            bgcolor: "#006649",
            
          }}>
              
              <Button color='inherit'  component={Link} to="/" 
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                
                
              }}>Home</Button>
              
              <Button color='inherit' startIcon="" component={Link} to="/addApplicant"
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                
              }}
              >Register</Button>

              <Button color='inherit' startIcon="" component={Link} to="/JobApplication"
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                
              }}
              >Edit Profile</Button>

              <Button color='inherit' startIcon="" component={Link} to="/review"
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                
              }}
              >Review Dashboard</Button>
 
          <Button color="inherit" component={Link} to="/login"
          sx={{
            p: 2,
            px: 4,
            marginLeft: "auto",
            display: "flex", 
            flexWrap: "wrap",
            justifyContent: "space-between",
          }}
          >Login</Button>
        </Toolbar>
      </AppBar>
    
  );
}
