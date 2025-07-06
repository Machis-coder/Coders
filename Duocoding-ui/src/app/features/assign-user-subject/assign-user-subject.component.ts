import { Component, OnInit } from '@angular/core'

import {UserSubjectService} from "../../core/services/usersubject.service";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {AssignUserSubjectRequestDTO} from "../../interfaces/AssignUserSubjectRequestDTO";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";



@Component({
  selector: 'app-assign-user-subject',
  standalone: true, //
  imports: [CommonModule, FormsModule, HomeNavigationComponent],
  templateUrl: './assign-user-subject.component.html',
  styleUrls: ['./assign-user-subject.component.css']
})
export class AssignUserSubjectComponent implements OnInit {
  userId: number = null;
  subjectId: number = null;

  userSubjects: UserSubjectResponseDTO[] = [];
  subjectUsers: UserSubjectResponseDTO[] = [];

  message: string = '';

  constructor(private userSubjectService: UserSubjectService) {}

  ngOnInit(): void {}

  getSubjectsByUser() {
    this.userSubjectService.getSubjectsByUser(this.userId).subscribe((res) => {
      this.userSubjects = res;
      this.message = `Asignaturas del usuario ${this.userId} cargadas`;
    });
  }

  getUsersBySubject() {
    this.userSubjectService.getUsersBySubject(this.subjectId).subscribe((res) => {
      this.subjectUsers = res;
      this.message = `Usuarios de la asignatura ${this.subjectId} cargados`;
    });
  }

  assign() {
    const dto: AssignUserSubjectRequestDTO = { userId: this.userId, subjectId: this.subjectId };
    this.userSubjectService.postAssignUser(dto).subscribe(() => {
      this.message = 'Usuario asignado a la asignatura.';
    });
  }

  activate() {
    const dto: AssignUserSubjectRequestDTO = { userId: this.userId, subjectId: this.subjectId };
    this.userSubjectService.activateRelation(dto).subscribe(() => {
      this.message = 'Relación activada.';
    });
  }

  delete() {
    const dto: AssignUserSubjectRequestDTO = { userId: this.userId, subjectId: this.subjectId};
    this.userSubjectService.deleteRelation(dto).subscribe(() => {
      this.message = 'Relación eliminada.';
    });
  }
}
