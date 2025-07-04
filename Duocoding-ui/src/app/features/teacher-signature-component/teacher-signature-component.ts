import { CommonModule } from '@angular/common';
import {Component, inject, OnInit} from '@angular/core';
import { BasePage } from '../base.page';

import {UserSubjectService} from "../../core/services/usersubject.service";
import {Router} from "@angular/router";
import {UserSubjectResponseDTO} from "../../interfaces/UserSubjectResponseDTO";
import {getUser} from "../../core/services/utils.service";
import {TestexecutionService} from "../../core/services/testexecution.service";
import {HomeNavigationComponent} from "../../shared/components/home-navigation-component/home-navigation-component";
import {TestExecutionGeneralDTO} from "../../interfaces/TestExecutionGeneralDTO";
import {TestService} from "../../core/services/test.service";
import {TestResponseDTO} from "../../interfaces/TestResponseDTO";

@Component({
  selector: 'app-teacher-signature',
  imports: [CommonModule, HomeNavigationComponent],
  templateUrl: './teacher-signature-component.html',
  standalone: true,
  styleUrl: './teacher-signature-component.css'
})
export class TeacherSignatureComponent extends BasePage implements OnInit {

  userSubjectService = inject(UserSubjectService);
  router = inject(Router);
  testExecutionService = inject(TestexecutionService);
  testService= inject(TestService);

  subjects: UserSubjectResponseDTO[] = [];
  selectedSubjectId: number | null = null;

  enrolledStudents: UserSubjectResponseDTO[] = [];

  loadingStudents = false;
  errorStudents: string | null = null;

  testExecutions: TestExecutionGeneralDTO[] = [];
  loadingExecutions = false;
  errorExecutions: string | null = null;

  selectedStudentId: number | null = null;

  availableTests: TestResponseDTO[] = [];
  loadingTests = false;
  errorTests: string | null = null;
  testsWereLoaded = false;

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

  loadTestExecutions(userId: number, subjectId: number) {
    this.loadingExecutions = true;
    this.errorExecutions = null;
    this.testExecutions = [];

    this.testExecutionService.getActiveTestExecutionsByUserAndSubject(userId, subjectId).subscribe({
      next: (data) => {
        console.log('Test executions recibidos:', data);  // <-- Aquí
        this.testExecutions = data || [];
        this.loadingExecutions = false;
      },
      error: (err) => {
        console.error('Error cargando exámenes', err);
        this.errorExecutions = 'Error al cargar exámenes';
        this.loadingExecutions = false;
      }
    });
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

  loadAvailableTests(subjectId: number) {
    this.loadingTests = true;
    this.errorTests = null;

    this.testService.getAvailableTestsBySubject(subjectId).subscribe({
      next: data => {
        console.log('Tests disponibles recibidos:', data);
        this.availableTests = data || [];
        this.loadingTests = false;
      },
      error: err => {
        console.error('Error al cargar tests disponibles', err);
        this.errorTests = 'Error al cargar tests disponibles';
        this.loadingTests = false;
      }
    });
  }

  onSubjectDblClick(subject: UserSubjectResponseDTO) {
    this.selectedSubjectId = subject.subjectId;
    this.testsWereLoaded = true;
    this.loadAvailableTests(subject.subjectId);
  }

  onStudentDblClick(student: UserSubjectResponseDTO) {
    this.selectedStudentId = student.userId;
    if (this.selectedSubjectId && this.selectedStudentId) {
      this.loadTestExecutions(this.selectedStudentId, this.selectedSubjectId);
    }
  }

  onExecutionClick(execution: TestExecutionGeneralDTO) {
    this.router.navigate(['/test-review-teacher', execution.id]);
  }

  onAvailableTestClick(test: TestResponseDTO) {
    // this.router.navigate(['/test-detail-view', test.id]); // Esperando implementación
    console.log('Vista de test no implementada aún, test ID:', test.id);
  }
}
