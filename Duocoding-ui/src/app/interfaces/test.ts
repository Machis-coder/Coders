import { Question } from "./question";

export interface Test {
    id: number,
    name: string,
    description: string,
    subject: number,
    questions: Question[]
}