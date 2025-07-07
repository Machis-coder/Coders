import { QuestionType } from "../shared/enums/question.types";
import { Response } from "./response";

export interface Question {
    id:number,
    order: number,
    type: QuestionType,
    description: string,
    answer: string,
    responses: Response[],
}
