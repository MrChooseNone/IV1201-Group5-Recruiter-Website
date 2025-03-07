
import './App.css';
import { HashRouter as Router, Routes, Route } from 'react-router-dom';
import Home from "./pages/Home";


import JobApplication from "./pages/JobApplication";
import NoPage from "./pages/NoPage";
import CompetencesPage from './pages/CompetencesPage';
import AddApplicant from './pages/AddApplicant';
import ReviewerDashboard from './pages/ReviewerDashboard';
import ApplicantDetails from './pages/ApplicantDetails';
import LoginPage from './pages/LoginPage';

import { createContext, useState, useMemo } from 'react';

function App() {

  const [auth, setAuth] = useState({ role: null, token: null });

  //Link https://stackoverflow.com/questions/57849977/reactjs-hooks-how-to-usecontext-in-two-different-js-files
  const authContextValue = useMemo(() => {
    return {
      auth,
      setAuth
    }
  }, [auth, setAuth]);

  return (
    <AuthContext.Provider value={authContextValue}> {/*This provides access to the auth object for every component within it*/}
      <Router>
        <Routes>
          {/*Always accessible pages */}
          <Route index element={<Home />} /> {/*Default end point "/" */}
          <Route path='/home' element={<Home />} />          
          <Route path='/addApplicant' element={<AddApplicant />} />
          <Route path='/login' element={<LoginPage />} />

          {/*Must be logged in */}
          <Route path='/JobApplication' element={<JobApplication />} />

          {/* Must be a reviewer*/}
          <Route path='/review' element={<ReviewerDashboard />} />
          <Route path='/applicant/:id' element={<ApplicantDetails />} />

          <Route path='*' element={<NoPage />} /> {/*Catch wrong URL end points to a error page */}
        </Routes>
      </Router>
    </AuthContext.Provider>
  );
}

export default App;

export const AuthContext = createContext(null); //This creates a context, to be used for handling authentication
