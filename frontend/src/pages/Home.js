import Appbar from "../components/Appbar";
import SearchApplication from "../components/SearchApplication";
import Competences from "../components/Competences";
import HomePageComp from "../components/HomePageComp";


export default function Home(){
    return (
        <>
            <Appbar/>
            <HomePageComp>
            </HomePageComp>
            <SearchApplication></SearchApplication>
            <Competences/>
        </>
    )
}