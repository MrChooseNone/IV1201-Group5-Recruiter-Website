import Appbar from "../components/Appbar";
import Competences from "../components/Competences";
import CompetencesByID from "../components/CompetencesByID";
import LanguageTranslate from "../components/LanguageTranslate";


export default function CompetencesPage(){
    return (
        <>
            <Appbar/>
            <Competences/>
            <CompetencesByID></CompetencesByID>
            <LanguageTranslate/>
        </>
    )
}