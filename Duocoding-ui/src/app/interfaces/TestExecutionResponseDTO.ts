export interface TestExecutionResponseDTO{

    id: number;
    questionId: number;
    answer: string;
    isCorrect: boolean;
    notes: string;
    questionDescription: string;
    questionAnswer: string;
    questionType: string;
}