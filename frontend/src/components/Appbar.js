import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import {Link} from 'react-router-dom';

import { AuthContext } from '../App';
import { useContext } from "react";

/**
 * This function represents the header bar, and is used for navigation and signing out
 * @returns The appbar component
 */
export default function Appbar() {

  //Here we import the auth information from the context
  const { auth, setAuth } = useContext(AuthContext);
  
  //This function signs out the user, by using the context function setAuth to an empty object
  function logOut()
  {
    setAuth({});
    alert("Signed Out");
    //TODO remove the stored token, from where it is stored
  }

  //This function renders the login button, if not logged in renders a button to go to the login screen, if logged in renders a button which sign out the user using the setAuth context function
  function renderLoginButton()
  {
    if(auth.token)
    {
      return <Button color="inherit" onClick={() => {
        logOut();
      }}
      sx={{
        p: 2,
        px: 4,
        marginLeft: "auto",
        display: "flex", 
        flexWrap: "wrap",
        justifyContent: "space-between",
        
      }}
      >Sign Out</Button>
    }
    else
    {
      return           <Button color="inherit" component={Link} to="/login"
      sx={{
        p: 2,
        px: 4,
        marginLeft: "auto",
        display: "flex", 
        flexWrap: "wrap",
        justifyContent: "space-between",
        
      }}
      >Login</Button>
    }
  }

    //This function renders the edit profile button if logged in or the register button if not logged in
    function renderEditProfileOrRegister()
    {
      if(auth.token)
      {
        return <Button color='inherit' startIcon="" component={Link} to="/JobApplication"
        sx={{
          p: 2,
          px: 4,
          display: "flex", 
          flexWrap: "wrap",
          justifyContent: "space-between",
          
        }}
        >Edit Profile</Button>
      }
      else
      {
        return <Button color='inherit' startIcon="" component={Link} to="/addApplicant"
        sx={{
          p: 2,
          px: 4,
          display: "flex", 
          flexWrap: "wrap",
          justifyContent: "space-between",
          
        }}
        >Register</Button>
      }
    }


    //This function renders the edit reviewer dashboard button if logged in as a reviewer
    function renderReviewerButton()
    {
      if(auth.role=="recruiter")
      {
        return <Button color='inherit' startIcon="" component={Link} to="/review"
        sx={{
          p: 2,
          px: 4,
          display: "flex", 
          flexWrap: "wrap",
          justifyContent: "space-between",
          
        }}
        >Review Dashboard</Button>
      }
    }

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
              
              {renderEditProfileOrRegister()}

              {renderReviewerButton()}

              {renderLoginButton()}
              
        </Toolbar>
      </AppBar>
    
  );
}
