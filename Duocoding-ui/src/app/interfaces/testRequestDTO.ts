import {QuestionRequestDTO} from "./questionRequestDTO";


export interface TestRequestDTO {
    name: string;
    description: string;
    subjectId: number;
    active: boolean;
    questions: QuestionRequestDTO[];
}