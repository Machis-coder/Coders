import { Question } from "../interfaces/question";
import { QuestionType } from "../shared/enums/question.types";

export const validateQuestion = (question: Question) =>{
    return question.type !== undefined && question.type !== null 
          && question.description !== '' && question.answer !== '' 
          && ((question.type !== QuestionType.MONOSELECTION && question.type !== QuestionType.MULTISELECTION) || question.responses.length > 0);
}