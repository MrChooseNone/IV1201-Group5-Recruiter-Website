
import './App.css';
import {Routes, Route} from 'react-router-dom';
import Home from "./pages/Home";

import JobApplication from "./pages/JobApplication";
import NoPage from "./pages/NoPage";
import CompetencesPage from './pages/CompetencesPage';
import AddApplicant from './pages/AddApplicant';
import ReviewerDashboard from './pages/ReviewerDashboard';
import ApplicationEndPoint from './components/ApplicationEndPoint';

function App() {
  return (
    
    
    <Routes>
      <Route index element={<Home/>}/> {/*Default end point "/" */}
      <Route path='/home'element={<Home/>}/>
      <Route path='/Competences' element={<CompetencesPage/>}/>
      <Route path='/JobApplication' element={<JobApplication/>}/>
      <Route path='/addApplicant' element={<AddApplicant/>}/>
      {/* add security*/ }
      <Route path='/review' element={<ReviewerDashboard/>}/>
      <Route path='/applicant/:id' element={<ApplicationEndPoint/>}/>

      <Route path='*' element={<NoPage/>}/> {/*Catch wrong URL end points to a error page */}
    </Routes>
    
  );
}

export default App;
