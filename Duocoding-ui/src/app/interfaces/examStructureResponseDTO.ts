import {QuestionRequestDTO} from "./questionRequestDTO";


export interface ExamStructureResponseDTO {
    testId: number;
    testTitle: string;
    questions: QuestionRequestDTO[];
}