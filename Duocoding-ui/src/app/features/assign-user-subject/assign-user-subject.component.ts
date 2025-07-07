import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";
import {User} from "../../interfaces/user";
import {SubjectResponseDTO} from "../../interfaces/subjectResponseDTO";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {UserSubjectService} from "../../core/services/usersubject.service";
import {UserService} from "../../core/services/user.service";
import {SubjectService} from "../../core/services/subject.service";
import {AssignUserSubjectRequestDTO} from "../../interfaces/AssignUserSubjectRequestDTO";

@Component({
  selector: 'app-assign-user-subject',
  standalone: true,
  imports: [CommonModule, FormsModule, HomeNavigationComponent],
  templateUrl: './assign-user-subject.component.html',
  styleUrls: ['./assign-user-subject.component.css']
})
export class AssignUserSubjectComponent implements OnInit {
  users: User[] = [];
  subjects: SubjectResponseDTO[] = [];

  selectedUser: User | null = null;
  selectedSubject: SubjectResponseDTO | null = null;

  userSubjects: UserSubjectResponseDTO[] = [];
  subjectUsers: UserSubjectResponseDTO[] = [];

  message: string = '';


  @ViewChild('userSubjectsSection') userSubjectsSection?: ElementRef;
  @ViewChild('subjectUsersSection') subjectUsersSection?: ElementRef;

  constructor(
      private userSubjectService: UserSubjectService,
      private userService: UserService,
      private subjectService: SubjectService
  ) {}

  ngOnInit(): void {
    this.loadUsers();
    this.loadSubjects();
  }

  loadUsers() {
    this.userService.findUsers().subscribe({
      next: res => this.users = res.body || [],
      error: err => this.message = 'Error cargando usuarios.'
    });
  }

  loadSubjects() {
    this.subjectService.findSubjects().subscribe({
      next: res => this.subjects = res.body || [],
      error: err => this.message = 'Error cargando asignaturas.'
    });
  }

  getSubjectsByUser() {
    if (!this.selectedUser) {
      this.message = 'Selecciona un usuario.';
      return;
    }

    this.userSubjectService.getSubjectsByUser(this.selectedUser.id).subscribe({
      next: res => {
        this.userSubjects = res;
        const username = this.selectedUser?.username || '';
        this.message = this.userSubjects.length > 0
            ? `Asignaturas del usuario ${username} cargadas.`
            : `El usuario ${username} no tiene asignaturas asignadas.`;

        setTimeout(() => {
          const section = this.userSubjectsSection?.nativeElement;
          section?.scrollIntoView({ behavior: 'smooth' });
          section?.classList.add('highlight');
          setTimeout(() => section?.classList.remove('highlight'), 2000);
        }, 0);
      },
      error: () => this.message = 'Error al obtener asignaturas del usuario.'
    });
  }

  getUsersBySubject() {
    if (!this.selectedSubject) {
      this.message = 'Selecciona una asignatura.';
      return;
    }
    this.userSubjectService.getUsersBySubject(this.selectedSubject.id).subscribe({
      next: res => {
        this.subjectUsers = res;
        const subjectName = this.selectedSubject?.name || '';
        this.message = this.subjectUsers.length > 0
            ? `Usuarios de la asignatura ${subjectName} cargados.`
            : `La asignatura ${subjectName} no tiene usuarios asignados.`;

        setTimeout(() => {
          const section = this.subjectUsersSection?.nativeElement;
          section?.scrollIntoView({ behavior: 'smooth' });
          section?.classList.add('highlight');
          setTimeout(() => section?.classList.remove('highlight'), 2000);
        }, 0);
      },
      error: () => this.message = 'Error al obtener usuarios de la asignatura.'
    });
  }
  assign() {
    if (!this.selectedUser || !this.selectedSubject) {
      this.message = 'Debes seleccionar un usuario y una asignatura.';
      return;
    }

    const dto: AssignUserSubjectRequestDTO = {
      userId: this.selectedUser.id,
      subjectId: this.selectedSubject.id
    };

    this.userSubjectService.postAssignUser(dto).subscribe({
      next: () => this.message = 'Usuario asignado a la asignatura.',
      error: () => this.message = 'Error al asignar usuario.'
    });
  }

  activate() {
    if (!this.selectedUser || !this.selectedSubject) {
      this.message = 'Debes seleccionar un usuario y una asignatura.';
      return;
    }

    const dto: AssignUserSubjectRequestDTO = {
      userId: this.selectedUser.id,
      subjectId: this.selectedSubject.id
    };

    this.userSubjectService.activateRelation(dto).subscribe({
      next: () => this.message = 'Relación activada.',
      error: () => this.message = 'Error al activar relación.'
    });
  }

  delete() {
    if (!this.selectedUser || !this.selectedSubject) {
      this.message = 'Debes seleccionar un usuario y una asignatura.';
      return;
    }

    const dto: AssignUserSubjectRequestDTO = {
      userId: this.selectedUser.id,
      subjectId: this.selectedSubject.id
    };

    this.userSubjectService.deleteRelation(dto).subscribe({
      next: () => this.message = 'Relación eliminada.',
      error: () => this.message = 'Error al eliminar relación.'
    });
  }
}