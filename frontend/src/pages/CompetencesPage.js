import Appbar from "../components/Appbar";
import Competences from "../components/Competences";
import CompetencesByID from "../components/CompetencesByID";
import LanguageTranslate from "../components/LanguageTranslate";
import SearchApplication from "../components/SearchApplication";
import FollowMouse from "../components/FollowMouse";


export default function CompetencesPage(){
    return (
        <>
            <Appbar/>
            <FollowMouse></FollowMouse>
            <SearchApplication/>
            <LanguageTranslate/>
        </>
    )
}