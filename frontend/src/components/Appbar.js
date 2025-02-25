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
              
              <Button color='inherit' startIcon="" component={Link} to="/" 
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                gap: 4,
              }}>Home</Button>
              
              
              <Button color='inherit' startIcon="" component={Link} to="/competences"
              sx={{
                p: 2,
                px: 4,
                alignSelf: "right",
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                gap: 4,
              }}>Competences</Button>
              
              
              <Button color='inherit' startIcon="" component={Link} to="/addApplicant"
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                gap: 4,
              }}
              >Register</Button>

              <Button color='inherit' startIcon="" component={Link} to="/JobApplication"
              sx={{
                p: 2,
                px: 4,
                display: "flex", 
                flexWrap: "wrap",
                justifyContent: "space-between",
                gap: 4,
              }}
              >Edit Profile</Button>

              
              
          <Button color="inherit" 
          sx={{
            p: 2,
            px: 4,
            marginLeft: "auto",
            display: "flex", 
            flexWrap: "wrap",
            justifyContent: "space-between",
            gap: 4,
          }}
          >Login</Button>
        </Toolbar>
      </AppBar>
    
  );
}
