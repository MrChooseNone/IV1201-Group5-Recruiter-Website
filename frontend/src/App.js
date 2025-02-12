
import './App.css';
import {Routes, Route} from 'react-router-dom';
import Home from "./pages/Home";
import About from "./pages/About";
import JobApplication from "./pages/JobApplication";
import NoPage from "./pages/NoPage";

function App() {
  return (
    
    
    <Routes>
      <Route index element={<Home/>}/> {/*Default end point "/" */}
      <Route path='/home'element={<Home/>}/>
      <Route path='/about' element={<About/>}/>
      <Route path='/JobApplication' element={<JobApplication/>}/>
      <Route path='*' element={<NoPage/>}/> {/*Catch wrong URL end points to a error page */}
    </Routes>
    
  );
}

export default App;
