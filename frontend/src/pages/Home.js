import Appbar from "../components/Appbar";

import Competences from "../components/Competences";
import Grid from "../components/Grid";
import HomePageComp from "../components/HomePageComp";
import FollowMouse from "../components/FollowMouse";


export default function Home(){
    return (
        <>
            <Appbar/>
            <div style={{
                height: "100vh"
            }}>

                <FollowMouse></FollowMouse>
                <HomePageComp>
                </HomePageComp>
            </div>
            
            
        </>
    )
}