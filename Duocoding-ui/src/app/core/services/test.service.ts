import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import ConstUrls from "../../shared/contants/const-urls";
import {Observable} from "rxjs";

import {TestResponseDTO} from "../../interfaces/TestResponseDTO";
import {TestRequestDTO} from "../../interfaces/testRequestDTO";
import {ExamStructureResponseDTO} from "../../interfaces/examStructureResponseDTO";

@Injectable ({
    providedIn: 'root'
})
export class TestService {
    constructor(private http: HttpClient) {
    }

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_TEST;

    getAllTest(): Observable<TestResponseDTO[]> {
        return this.http.get<TestResponseDTO[]>(`${this.END_POINT_URL}/`);
    }
    getTestById(testId: number): Observable<TestResponseDTO> {
        return this.http.get<TestResponseDTO>(`${this.END_POINT_URL}/${testId}`);
    }

    createTest(testDetails: TestRequestDTO): Observable<TestResponseDTO> {
        return this.http.post<TestResponseDTO>(`${this.END_POINT_URL}/create`, testDetails);
    }

    updateTest(id: number, testDetails: TestRequestDTO): Observable<TestResponseDTO> {
        return this.http.put<TestResponseDTO>(`${this.END_POINT_URL}/${id}`, testDetails);
    }

    deleteTest(id: number): Observable<string> {
        return this.http.put(`${this.END_POINT_URL}/${id}/delete`, null, { responseType: 'text' });
    }

    activateTest(id: number): Observable<string> {
        return this.http.put(`${this.END_POINT_URL}/${id}/activate`, null, { responseType: 'text' });
    }

    getExamStructure(testId: number): Observable<ExamStructureResponseDTO> {
        return this.http.get<ExamStructureResponseDTO>(`${this.END_POINT_URL}/${testId}/structure`);
    }
}
