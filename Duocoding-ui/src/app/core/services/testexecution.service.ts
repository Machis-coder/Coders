import {Injectable} from "@angular/core";
import {HttpClient, HttpResponse} from "@angular/common/http";
import ConstUrls from "../../shared/contants/const-urls";
import {Observable} from "rxjs";

import {TestExecutionRequestDTO} from "../../interfaces/TestExecutionRequestDTO";
import {TestExecutionGeneralDTO} from "../../interfaces/TestExecutionGeneralDTO";
import {NotesFromTeacherRequestDTO} from "../../interfaces/NotesFromTeacherRequestDTO";

@Injectable ({
    providedIn: 'root'
})
export class TestexecutionService {
    constructor(private http: HttpClient) {}

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_TESTEXECUTION;

    getAllTestExecutions(): Observable<TestExecutionGeneralDTO[]> {
        return this.http.get<TestExecutionGeneralDTO[]>(`${this.END_POINT_URL}/`);
    }

    getDeletedTestExecutions(): Observable<TestExecutionGeneralDTO[]> {
        return this.http.get<TestExecutionGeneralDTO[]>(`${this.END_POINT_URL}/false`);
    }
    getTestExecutionFULLById(id: number): Observable<TestExecutionGeneralDTO> {
        return this.http.get<TestExecutionGeneralDTO>(`${this.END_POINT_URL}/id/${id}`);
    }
    getActiveTestExecutionsByUserAndSubject(userId: number, subjectId: number): Observable<TestExecutionGeneralDTO[]> {
        return this.http.get<TestExecutionGeneralDTO[]>(`${this.END_POINT_URL}/users/${userId}/subjects/${subjectId}/executions`);
    }


    saveTestExecution(dto: TestExecutionRequestDTO): Observable<void> {
        return this.http.post<void>(`${this.END_POINT_URL}/`, dto);
    }

    saveTestExecutionNotes(dto: NotesFromTeacherRequestDTO): Observable<void> {
        return this.http.post<void>(`${this.END_POINT_URL}/notes`, dto);
    }

    getTestExecutionsByUserId(userId: number): Observable<TestExecutionGeneralDTO[]> {
        return this.http.get<TestExecutionGeneralDTO[]>(`${this.END_POINT_URL}/users/${userId}/executions`);
    }

    deleteTestExecution(id: number): Observable<void> {
        return this.http.put<void>(`${this.END_POINT_URL}/${id}/delete`, {});
    }

    activateTestExecution(id: number): Observable<void> {
        return this.http.put<void>(`${this.END_POINT_URL}/${id}/activate`, {});
    }

}