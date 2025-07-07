export interface UserSubjectResponseDTO {
    userId: number;
    userName: string;
    subjectId: number;
    subjectName: string;
    role: string;
    surname: string;
    email?: string;
    dni: string;
    name: string;
    active: boolean;
}