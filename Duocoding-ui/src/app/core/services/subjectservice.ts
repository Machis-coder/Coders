import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import ConstUrls from '../../shared/contants/const-urls';
import { SubjectRequestDTO } from 'src/app/interfaces/subjectRequestDTO';
import { SubjectResponseDTO } from 'src/app/interfaces/subjectResponseDTO';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SubjectService {
private END_POINT_URL = ConstUrls.API_URL + ConstUrls.CONTEXT_SUBJECT;

  constructor(private http: HttpClient) {}

  // Obtener todos los subjects
  findSubjects(): Observable<HttpResponse<SubjectResponseDTO[]>> {
    return this.http.get<SubjectResponseDTO[]>(this.END_POINT_URL + '/', {
      observe: 'response',
    });
  }

  // Obtener subject por id
  findSubject(id: number): Observable<HttpResponse<SubjectResponseDTO>> {
    return this.http.get<SubjectResponseDTO>(this.END_POINT_URL + '/' + id, {
      observe: 'response',
    });
  }

  // Obtener subject por nombre
  findSubjectByName(name: string): Observable<HttpResponse<SubjectResponseDTO>> {
    return this.http.get<SubjectResponseDTO>(
      this.END_POINT_URL + '/name/' + name,
      {
        observe: 'response',
      }
    );
  }

  // Crear nuevo subject
  save(subject: SubjectRequestDTO): Observable<HttpResponse<void>> {
    return this.http.post<void>(this.END_POINT_URL + '/', subject, {
      observe: 'response',
    });
  }

  // Actualizar subject por id
  update(id: number, subject: SubjectRequestDTO): Observable<HttpResponse<void>> {
    return this.http.put<void>(this.END_POINT_URL + '/' + id, subject, {
      observe: 'response',
    });
  }

  // Eliminar (desactivar) subject por id
  delete(id: number): Observable<HttpResponse<void>> {
    return this.http.put<void>(this.END_POINT_URL + '/' + id + '/delete', null, {
      observe: 'response',
    });
  }

  // Activar subject por id
  activate(id: number): Observable<HttpResponse<void>> {
    return this.http.put<void>(this.END_POINT_URL + '/' + id + '/activate', null, {
      observe: 'response',
    });
  }

  // Obtener subjects inactivos
  findInactiveSubjects(): Observable<HttpResponse<SubjectResponseDTO[]>> {
    return this.http.get<SubjectResponseDTO[]>(this.END_POINT_URL + '/inactive', {
      observe: 'response',
    });
  }
}
