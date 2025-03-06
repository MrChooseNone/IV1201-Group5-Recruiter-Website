
import './App.css';
import {HashRouter as Router, Routes, Route} from 'react-router-dom';
import Home from "./pages/Home";

import JobApplication from "./pages/JobApplication";
import NoPage from "./pages/NoPage";
import CompetencesPage from './pages/CompetencesPage';
import AddApplicant from './pages/AddApplicant';
import ReviewerDashboard from './pages/ReviewerDashboard';
import ApplicantDetails from './pages/ApplicantDetails';
import LoginPage from './pages/LoginPage';


function App() {
  return (
    
    <Router>
    <Routes>
      <Route index element={<Home/>}/> {/*Default end point "/" */}
      <Route path='/home'element={<Home/>}/>
      
      <Route path='/JobApplication' element={<JobApplication/>}/>
      <Route path='/addApplicant' element={<AddApplicant/>}/>
      <Route path='/login' element={<LoginPage/>}/>
      {/* add security*/ }
      
      <Route path='/review' element={<ReviewerDashboard/>}/>
      <Route path='/applicant/:id' element={<ApplicantDetails/>}/>

      <Route path='*' element={<NoPage/>}/> {/*Catch wrong URL end points to a error page */}
    </Routes>
    </Router>
    
  );
}

export default App;
