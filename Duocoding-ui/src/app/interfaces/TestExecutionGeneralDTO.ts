import {TestExecutionResponseDTO} from "./TestExecutionResponseDTO";

export interface TestExecutionGeneralDTO {
    id: number;
    testId: number;
    userId: number;
    date: string;
    startTime: string;
    endTime: string;
    result: number;
    notes: string;
    executionResponsesList?: TestExecutionResponseDTO[];
    testName: string;
}