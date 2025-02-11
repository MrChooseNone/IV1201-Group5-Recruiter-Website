import Appbar from "../components/Appbar";
import SearchApplication from "../components/SearchApplication";



export default function Home(){
    return (
        <>
            <Appbar/>
            <h2>Home page</h2>
            <SearchApplication></SearchApplication>
        </>
    )
}