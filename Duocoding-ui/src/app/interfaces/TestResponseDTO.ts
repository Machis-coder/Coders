import {QuestionResponseDTO} from "./questionResponseDTO";

export interface TestResponseDTO {
    id:number;
    name:string;
    description: string;
    active:boolean;
    subjectId:number;
    questions: QuestionResponseDTO[];

}