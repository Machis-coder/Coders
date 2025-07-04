import {ResponseDTO} from "./responseDTO";

export interface QuestionResponseDTO {
    id: number;
    answer: string;
    questiontype: string;
    description: string;
    responses: ResponseDTO[];
}