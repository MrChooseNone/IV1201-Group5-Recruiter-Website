import Appbar from "../components/Appbar";

import Competences from "../components/Competences";
import Grid from "../components/Grid";
import HomePageComp from "../components/HomePageComp";
import FollowMouse from "../components/FollowMouse";


export default function Home(){
    return (
        <>
            <Appbar/>
            

                <FollowMouse></FollowMouse>
                <HomePageComp>
                </HomePageComp>
            
            
            
        </>
    )
}