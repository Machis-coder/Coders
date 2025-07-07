import { QuestionType } from "../shared/enums/question.types";
import { ResponseExecution } from "./response-execution";

export interface QuestionExecution {
    id:number,
    order: number,
    type: QuestionType,
    description: string,
    answer: string,
    userAnswer: string,
    correct: boolean,
    responses: ResponseExecution[]
}