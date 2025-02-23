import Appbar from "../components/Appbar";
import Competences from "../components/Competences";
import CompetencesByID from "../components/CompetencesByID";
import LanguageTranslate from "../components/LanguageTranslate";
import SearchApplication from "../components/SearchApplication";


export default function CompetencesPage(){
    return (
        <>
            <Appbar/>
            <SearchApplication/>
            <LanguageTranslate/>
        </>
    )
}