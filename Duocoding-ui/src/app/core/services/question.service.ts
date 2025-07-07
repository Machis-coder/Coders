import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import ConstUrls from "../../shared/contants/const-urls";
import { Question } from 'src/app/interfaces/question';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  constructor(private http: HttpClient) {}

    private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_QUESTION;

    findQuestions(): Observable<HttpResponse<Question[]>> {
        return this.http.get<Question[]>(this.END_POINT_URL + '/', {
                    //headers: headers,
                    observe: 'response'
                });
    }

    findQuestion(id: number) {
         return this.http.get<Question>(this.END_POINT_URL + '/' + id, {
                    //headers: headers,
                    observe: 'response'
                });
    }

    save(user: Question) {
        return this.http.post<Question>(this.END_POINT_URL + '/', user, {
                    //headers: headers,
                    observe: "response",
                });
    }  

    update(user: Question) {
        return this.http.put<Question>(this.END_POINT_URL + '/', user, {
                    //headers: headers,
                    observe: "response",
                });
    }

}
