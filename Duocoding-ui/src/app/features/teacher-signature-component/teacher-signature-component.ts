import { CommonModule } from '@angular/common';
import {Component, inject, OnInit} from '@angular/core';
import { BasePage } from '../base.page';

import {UserSubjectService} from "../../core/services/usersubject.service";
import {Router} from "@angular/router";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {getUser} from "../../core/services/utils.service";
import {TestExecutionService} from "../../core/services/TestExecution.service";

@Component({
  selector: 'app-teacher-signature',
  imports: [CommonModule],
  templateUrl: './teacher-signature-component.html',
  standalone: true,
  styleUrl: './teacher-signature-component.css'
})
export class TeacherSignatureComponent extends BasePage implements OnInit {

  userSubjectService = inject(UserSubjectService);
  router = inject(Router);
  testExecutionService = inject(TestExecutionService);

  subjects: UserSubjectResponseDTO[] = [];
  selectedSubjectId: number | null = null;

  enrolledStudents: UserSubjectResponseDTO[] = [];

  loadingStudents = false;
  errorStudents: string | null = null;

  ngOnInit(): void {
    super.ngOnInit();

    const loggedUser = getUser();
    const userId = loggedUser?.id;

    if (userId) {
      this.userSubjectService.getSubjectsByUser(userId).subscribe({
        next: data => this.subjects = data,
        error: err => console.error('Error al cargar asignaturas', err)
      });
    }
  }

  onSelectSubject(subject: UserSubjectResponseDTO) {
    this.selectedSubjectId = subject.subjectId;
    this.loadStudents(subject.subjectId);
  }

  loadStudents(subjectId: number) {
    this.loadingStudents = true;
    this.errorStudents = null;

    this.userSubjectService.getUsersBySubject(subjectId).subscribe({
      next: data => {
        this.enrolledStudents = data;
        this.loadingStudents = false;
      },
      error: err => {
        console.error('Error al cargar alumnos', err);
        this.errorStudents = "Error al cargar alumnos";
        this.loadingStudents = false;
      }
    });
  }

  onSubjectDblClick(subject: UserSubjectResponseDTO) {
    this.router.navigate(['/subject-details', subject.subjectId]);
  }

  onStudentDblClick(student: UserSubjectResponseDTO) {
    if(this.selectedSubjectId) {
      this.router.navigate(['/student-tests', this.selectedSubjectId, student.userId]);
    }
  }
}
