
import { QuestionExecution } from "./question-execution";

export interface TestExecution {
    id: number,
    name: string,
    description: string,
    subject: number,
    questions: QuestionExecution[]
}