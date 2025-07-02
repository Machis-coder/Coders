import {Injectable} from "@angular/core";
import ConstUrls from "../../shared/contants/const-urls";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {AssignUserSubjectRequestDTO} from "../../interfaces/AssignUserSubjectRequestDTO";

@Injectable ({
    providedIn: 'root'
})
export class UserSubjectService {
    private END_POINT_URL = ConstUrls.API_URL + '/usersubject';

    constructor(private http: HttpClient) {}

    getSubjectsByUser (userId: number): Observable<UserSubjectResponseDTO[]>{
        return this.http.get<UserSubjectResponseDTO[]>(`${this.END_POINT_URL}/user/${userId}`);
    }

    getUsersBySubject (subjectId:number): Observable<UserSubjectResponseDTO[]>{
        return this.http.get<UserSubjectResponseDTO[]>(`${this.END_POINT_URL}/subject/${subjectId}`)
    }

    postAssignUser (dto: AssignUserSubjectRequestDTO): Observable<any>{
        return this.http.post(`${this.END_POINT_URL}/assign`, dto);
    }

    activateRelation (dto:AssignUserSubjectRequestDTO): Observable<any>{
        return this.http.put(`${this.END_POINT_URL}/activate`, dto);
    }
    deleteRelation (dto:AssignUserSubjectRequestDTO): Observable<any>{
        return this.http.put(`${this.END_POINT_URL}/delete`, dto);
    }
}