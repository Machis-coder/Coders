import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import ConstUrls from "../../shared/contants/const-urls";
import { Question } from 'src/app/interfaces/question';
import { Observable } from 'rxjs';
import { Test } from 'src/app/interfaces/test';
import { TestExecution } from 'src/app/interfaces/test.execution';


@Injectable({
  providedIn: 'root'
})
export class TestExecutionService {
  constructor(private http: HttpClient) {}

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_TEST_EXECUTION

    findTests(): Observable<HttpResponse<Test[]>> {
        return this.http.get<Test[]>(this.END_POINT_URL + '/', {
                    //headers: headers,
                    observe: 'response'
                });
    }

    findTest(id: number) {
         return this.http.get<TestExecution>(this.END_POINT_URL + '/' + id + '/review', {
                    //headers: headers,
                    observe: 'response'
                });
    }

    save(test: any) {
        return this.http.post<any>(this.END_POINT_URL + '/',test, {
                    //headers: headers,
                    observe: "response",
                });
    }

    update(test: Test) {
        return this.http.put<Test>(this.END_POINT_URL + '/', test, {
                    //headers: headers,
                    observe: "response",
                });
    }

}
